// Copyright 2016 NetworkedAssets Sp. z o.o.
// This file is part of the Arc42-Template-Plugin for Atlassian Confluence.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Built with the open content of the Arc42 software architecture structure of Dr. Gernot Starke and Dr. Peter Hruschka, http://www.arc42.org

package com.networkedassets.plugins.space_blueprint;

import com.atlassian.confluence.labels.LabelManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.util.LabelUtil;
import com.atlassian.confluence.util.i18n.I18NBean;
import com.atlassian.confluence.util.i18n.I18NBeanFactory;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * This page adder adds a pagetree to a given root page.
 *
 * @author Mikołaj Robakowski
 */
public class SpaceBlueprintPageAdder {
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(SpaceBlueprintPageAdder.class);
    private InputStream atlassianPlugin;
    private LabelManager labelManager;
    private PageManager pageManager;
    private I18NBean i18n;

    public SpaceBlueprintPageAdder(PageManager pm, InputStream atlasPlugin, I18NBeanFactory i18,
                                   LabelManager labelManager) {
        pageManager = pm;
        atlassianPlugin = atlasPlugin;
        this.labelManager = labelManager;
        i18n = i18.getI18NBean();
    }

    private Document loadAndParse() throws IOException {
        return Jsoup.parse(atlassianPlugin, null, "", Parser.xmlParser());
    }

    public void addPages(Long pageId, boolean isLabeled, boolean overwrite) {
        Page rootPage = pageManager.getPage(pageId);
        Document document;

        if (rootPage == null) {
            throw new RuntimeException("root page is null");
        }

        try {
            document = loadAndParse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e); // like I care
        }

        Elements spaceBlueprints = document.getElementsByTag("space-blueprint");

        if (spaceBlueprints.size() != 1) {
            throw new RuntimeException("wrong number of <space-blueprint> tags");
        }

        Element spaceBlueprint = spaceBlueprints.get(0);
        Element root = null;
        for (Element e : spaceBlueprint.children()) {
            if (e.tag().getName().equals("content-template")) {
                if (root == null)
                    root = e;
                else
                    throw new RuntimeException("Not supported number of root <content-template>s");
            }
        }

        if (root == null)
            throw new RuntimeException("Root not found");

        try {
            if (overwrite)
                plantPageTree(root, rootPage, document, isLabeled);
            else
                plantChild(root, rootPage, document, isLabeled);
        } catch (IOException e) {
            throw new RuntimeException("Error while planting pages", e);
        }
    }

    private void plantPageTree(Element elem, Page rootPage, Document document, boolean isLabeled) throws IOException {
        Element templateDetails = document.select("content-template[key=\"" + elem.attr("ref") + "\"]").get(0);
        String titleKey = templateDetails.attr("i18n-name-key");
        String title = i18n.getText(titleKey);
        rootPage.setVersion(rootPage.getVersion() + 1);
        rootPage.setTitle(title);
        // TODO: probably set last modified date, modifier name, etc.
        String pagesXmlPath = templateDetails.getElementsByTag("resource").get(0).attr("location");
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(pagesXmlPath);
        String content = IOUtils.toString(is);
        rootPage.setBodyAsString(content);
        pageManager.saveContentEntity(rootPage, null);

        if (isLabeled) {
            LabelUtil.addLabel("arc42-added", labelManager, rootPage);
        }

        for (Element child : elem.children()) {
            if (child.tag().getName().equals("content-template"))
                plantChild(child, rootPage, document, isLabeled);
        }
    }

    private void plantChild(Element child, Page parentPage, Document document, boolean isLabeled) throws IOException {
        Page page = new Page();
        page.setParentPage(parentPage);
        page.setSpace(parentPage.getSpace());
        page.setVersion(0); // increments to 1 in plantPageTree
        parentPage.addChild(page);
        plantPageTree(child, page, document, isLabeled);
    }
}
