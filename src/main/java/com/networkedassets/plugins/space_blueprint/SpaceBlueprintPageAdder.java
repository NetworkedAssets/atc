package com.networkedassets.plugins.space_blueprint;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.util.i18n.I18NBean;
import com.atlassian.confluence.util.i18n.I18NBeanFactory;

public class SpaceBlueprintPageAdder {
	private Document document;
	private InputStream atlassianPlugin;
	private PageManager pageManager;
	private I18NBean i18n;
	private static Logger log = LoggerFactory.getLogger(SpaceBlueprintPageAdder.class);

	public SpaceBlueprintPageAdder(PageManager pm, InputStream atlasPlugin, I18NBeanFactory i18) {
		pageManager = pm;
		atlassianPlugin = atlasPlugin;
		i18n = i18.getI18NBean();
	}

	private void loadAndParse() throws IOException {
		document = Jsoup.parse(atlassianPlugin, null, "", Parser.xmlParser());
	}

	public void addPages(Long pageId) {
		Page rootPage = pageManager.getPage(pageId);

		if (rootPage == null) {
			throw new RuntimeException("root page is null");
		}

		try {
			loadAndParse();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e); // like I care
		}

		Elements spaceBlueprints = document.getElementsByTag("space-blueprint");

		if (spaceBlueprints.size() != 1) {
			throw new RuntimeException("wrong number of <space-blueprint> tags");
		}

		Element spaceBlueprint = spaceBlueprints.get(0);

		Elements els = spaceBlueprint.children();
		List<Element> es = new ArrayList<Element>();
		
		for (Element e : els) { // TODO: refactor this to stream filter asap
			if (e.tag().getName().equals("content-template"))
				es.add(e);
		}
		
		log.warn("!!!!!!!!!!!!!es.size === " + es.size());

		if (es.size() != 1)
			throw new RuntimeException("Not supported number of root <content-template>s");

		try {
			plantPageTree(es.get(0), rootPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error while planting pages", e);
		}
	}

	private void plantPageTree(Element elem, Page rootPage) throws IOException {
		Element templateDetails = document.select("content-template[key=\"" + elem.attr("ref") + "\"]").get(0);
		String titleKey = templateDetails.attr("i18n-name-key");
		String title = i18n.getText(titleKey);
		rootPage.setVersion(rootPage.getVersion() + 1);
		rootPage.setTitle(title);
		// TODO: probably set last modified date, modifier name, etc.
		String pagesXmlPath = templateDetails.getElementsByTag("resource").get(0).attr("location");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(pagesXmlPath);
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer, "utf-8");
		String content = writer.toString();
		rootPage.setBodyAsString(content);
		pageManager.saveContentEntity(rootPage, null);
		for (Element child : elem.children().select("content-template")) {
			plantChild(child, rootPage);
		}
	}

	private void plantChild(Element child, Page parentPage) throws IOException {
		Page page = new Page();
		page.setParentPage(parentPage);
		page.setSpace(parentPage.getSpace());
		page.setVersion(0); // increments to 1 in plantPageTree
		parentPage.addChild(page);
		plantPageTree(child, page);
	}
}
