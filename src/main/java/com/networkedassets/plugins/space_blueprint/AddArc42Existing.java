package com.networkedassets.plugins.space_blueprint;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.actions.AbstractSpaceAction;

@SuppressWarnings("serial")
public class AddArc42Existing extends AbstractSpaceAction {
	private static final String PAGES_ERROR = "pagesError";

	Long pageId;

	private PageManager pageManager;
	private PermissionManager permissionManager;

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(AddArc42Existing.class);

	public String addInExisting() {		
		if (!permissionManager.hasPermission(getAuthenticatedUser(), Permission.ADMINISTER, getSpace()))
			return ERROR;

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("atlassian-plugin.xml");		
		SpaceBlueprintPageAdder adder = new SpaceBlueprintPageAdder(pageManager,
				is, i18NBeanFactory);
		
		try {
			adder.addPages(pageId);
		} catch (Exception e) {
			return PAGES_ERROR;
		}

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

	public void setPermissionManager(PermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

}
