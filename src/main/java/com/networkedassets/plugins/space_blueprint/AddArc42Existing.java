// Copyright 2015 NetworkedAssets GmbH
// This file is part of the Arc42-Template-Plugin for Atlassian Confluence.

package com.networkedassets.plugins.space_blueprint;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.security.Permission;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.spaces.actions.AbstractSpaceAction;

/**
 * This class uses the page adder to add the Arc42 template to a given root page.
 * 
 * @author Mikołaj Robakowski
 *
 */
@SuppressWarnings("serial")
public class AddArc42Existing extends AbstractSpaceAction {
	/**A final String that will be displayed for page errors*/
	private static final String PAGES_ERROR = "pagesError";

	Long pageId;

	private PageManager pageManager;
	private PermissionManager permissionManager;

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(AddArc42Existing.class);

	/**Creates a PageAdder and tries to add the pagetree to the current page.
	 * 
	 * @return Success if pages could be added, or error if the space or page could not be found.
	 * */
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

	/**Returns the pageId
	 * 
	 * @return The pageId
	 * */
	public Long getPageId() {
		return pageId;
	}

	/**Sets the PageId
	 * 
	 * @param pageId The PageId
	 * */
	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	/**Sets the PageManager
	 * 
	 * @param pageManager The PageManager
	 * */
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	/**Sets the PermissionManager
	 * 
	 * @param permissionManager The PermissionManager
	 * */
	public void setPermissionManager(PermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

}
