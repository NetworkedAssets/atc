/**
 * Created by mrobakowski on 10/8/2015.
 */

AJS.toInit(function () {
    console.log("stuffff!!!!");

    var dialog = new AJS.Dialog({
        width: 600,
        height: 300,
        id: "example-dialog",
        closeOnOutsideClick: true
    });

    dialog.addHeader("Add ARC42 blueprint");
    dialog.addPanel("DefaultPanel", '\
        <form action="/plugins/space-blueprint/add-arc42-existing.action" method="get" id="add-arc42-dialog-form" class="aui">\
            <fieldset class="group">\
            <legend><span>Blueprint options</span></legend>\
            <div class="checkbox">\
                <input class="checkbox" type="checkbox" name="isLabeled" id="arc42isLabeled" checked="checked">\
                <label for="arc42isLabeled">Add "arc42-added" labels to inserted pages</label>\
            </div>\
            <div class="checkbox">\
                <input class="checkbox" type="checkbox" name="overwrite" id="arc42overwrite">\
                <label for="arc42overwrite">Overwrite current page instead of adding as a child</label>\
            </div>\
        \
            <input id="arc42spaceKey" type="hidden" value="foo" name="key" />\
            <input id="arc42pageId" type="hidden" value="-1" name="pageId" />\
            </fieldset>\
        </form>', "");

    dialog.addButton("Add", function() {
        AJS.$("#arc42spaceKey").val(AJS.Data.get("space-key"));
        AJS.$("#arc42pageId").val(Confluence.getContentId());
        document.getElementById("add-arc42-dialog-form").submit();
    });

    dialog.addLink("Close", function() {
        dialog.hide();
    });

    AJS.$("#add-arc42-existing-space-link-id").attr("href", "javascript:void(0)");

    AJS.$("#add-arc42-existing-space-link-id").click(function () {
        dialog.show();
    });

    ////////////////////////////////////////////////////////

    //AJS.$("#add-arc42-close-button").click(function (e) {
    //    e.preventDefault();
    //    AJS.dialog2("#add-arc42-dialog").hide();
    //});
    //
    //AJS.$("#add-arc42-add-button").click(function (e) {
    //    e.preventDefault();
    //    AJS.$("#arc42spaceKey").val(AJS.Data.get("space-key"));
    //    AJS.$("#arc42pageId").val(Confluence.getContentId());
    //    document.getElementById("add-arc42-dialog-form").submit();
    //})
});