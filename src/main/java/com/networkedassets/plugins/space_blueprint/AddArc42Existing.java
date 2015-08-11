package com.networkedassets.plugins.space_blueprint;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.actions.AbstractSpaceAction;

@SuppressWarnings("serial")
public class AddArc42Existing extends AbstractSpaceAction {
	Long pageId;

	private PageManager pageManager;
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(AddArc42Existing.class);

	public String addInExisting() {		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("atlassian-plugin.xml");		
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
