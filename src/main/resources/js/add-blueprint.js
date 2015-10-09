/**
 * Created by mrobakowski on 10/8/2015.
 */

AJS.toInit(function () {
    console.log("stuffff!!!!");
    AJS.$("#add-arc42-existing-space-link-id").click(function() {
        AJS.dialog2("#add-arc42-dialog").show();
    });

    AJS.$("#add-arc42-close-button").click(function(e) {
        e.preventDefault();
        AJS.dialog2("#add-arc42-dialog").hide();
    });

    AJS.$("#add-arc42-add-button").click(function(e) {
        e.preventDefault();
        AJS.$("#arc42spaceKey").val(AJS.Data.get("space-key"));
        AJS.$("#arc42pageId").val(Confluence.getContentId());
        document.getElementById("add-arc42-dialog-form").submit();
    })
});