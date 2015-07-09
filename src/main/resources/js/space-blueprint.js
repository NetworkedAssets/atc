/*Hook into the wizard apis. Update the space home page title to be the space name
  and a suffix of " Home Page".*/

AJS.bind("blueprint.wizard-register.ready", function () {
    function submitExampleSpace(e, state) {
        state.pageData.ContentPageTitle = state.pageData.name + " Home Page"; 
        return Confluence.SpaceBlueprint.CommonWizardBindings.submit(e, state);
    }
    function preRenderExampleSpace(e, state) {
        state.soyRenderContext['atlToken'] = AJS.Meta.get('atl-token');
        state.soyRenderContext['showSpacePermission'] = false; 
    }
   Confluence.Blueprint.setWizard('com.networkedassets.plugins.space-blueprint:space-blueprint-item', function(wizard) {
        wizard.on("submit.exampleSpaceId", submitExampleSpace);
        wizard.on("pre-render.exampleSpaceId", preRenderExampleSpace);
        wizard.on("post-render.exampleSpaceId", Confluence.SpaceBlueprint.CommonWizardBindings.postRender);
    });
});