/*Hook into the wizard apis. Update the space home page title to be the space name
  and a suffix of " Home Page".*/

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