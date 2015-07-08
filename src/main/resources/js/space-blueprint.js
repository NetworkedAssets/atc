AJS.bind("blueprint.wizard-register.ready", function () {
    function submitExampleSpace(e, state) {
        state.pageData.ContentPageTitle = state.pageData.name + " " + AJS.I18n.getText("confluence.blueprints.space.example.home.title.suffix"); 
        return Confluence.SpaceBlueprint.CommonWizardBindings.submit(e, state);
    }
    function preRenderExampleSpace(e, state) {
        state.soyRenderContext['atlToken'] = AJS.Meta.get('atl-token');
        state.soyRenderContext['showSpacePermission'] = false; 
    }
    Confluence.Blueprint.setWizard('com.example.plugins.tutorial.confluence.spacebp.spacebp:arc-space-blueprint-item', function(wizard) {
        wizard.on("submit.exampleSpaceId", submitExampleSpace);
        wizard.on("pre-render.exampleSpaceId", preRenderExampleSpace);
        wizard.on("post-render.exampleSpaceId", Confluence.SpaceBlueprint.CommonWizardBindings.postRender);
    });
});