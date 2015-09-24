// Copyright 2015 NetworkedAssets GmbH
// This file is part of the Arc42-Template-Plugin for Atlassian Confluence.

/** 
 * This script hooks into the wizard API. It adds the blueprint to the wizard and updates the space home page title to be the space name 
 * followed by the suffix "Home Page".
 *
 *  @author Tina Steiger  
 */

AJS.bind("blueprint.wizard-register.ready", function () {
    function submitArcSpace(e, state) {
        state.pageData.ContentPageTitle = state.pageData.name + " Home Page"; 
        return Confluence.SpaceBlueprint.CommonWizardBindings.submit(e, state);
    }
    function preRenderArcSpace(e, state) {
        state.soyRenderContext['atlToken'] = AJS.Meta.get('atl-token');
        state.soyRenderContext['showSpacePermission'] = false; 
    }
   Confluence.Blueprint.setWizard('com.networkedassets.plugins.space-blueprint:space-blueprint-item', function(wizard) {
        wizard.on("submit.arcSpaceId", submitArcSpace);
        wizard.on("pre-render.arcSpaceId", preRenderArcSpace);
        wizard.on("post-render.arcSpaceId", Confluence.SpaceBlueprint.CommonWizardBindings.postRender);
    });
});