package com.networkedassets.plugins.space_blueprint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.core.ConfluenceActionSupport;
import com.atlassian.confluence.pages.PageManager;
import com.google.common.base.Joiner;

@SuppressWarnings("serial")
public class AddArc42Existing extends ConfluenceActionSupport {
	Long pageId;

	private PageManager pageManager;
	private static Logger log = LoggerFactory.getLogger(AddArc42Existing.class);

	public String addInExisting() {
		String[] filesInCwd = new File(".").list();
		log.warn("!!!!!!!!!!!" + Joiner.on(", ").join(filesInCwd));
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("atlassian-plugin.xml");
//		StringWriter sw = new StringWriter();
//		try {
//			IOUtils.copy(is, sw, "utf-8");
//			log.warn(sw.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			is.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		is = this.getClass().getClassLoader().getResourceAsStream("atlassian-plugin.xml");
		
		SpaceBlueprintPageAdder adder = new SpaceBlueprintPageAdder(pageManager,
				is, i18NBeanFactory);
		adder.addPages(pageId);

		return SUCCESS;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

}
