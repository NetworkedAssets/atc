// Copyright 2016 NetworkedAssets Sp. z o.o.
// This file is part of the Arc42-Template-Plugin for Atlassian Confluence.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Built with the open content of the Arc42 software architecture structure of Dr. Gernot Starke and Dr. Peter Hruschka, http://www.arc42.org

/**
 * This script creates a dialog to make configurations while adding a pagetree to an existing space.
 *
 * @author Mikołaj Robakowski on 10/8/2015.
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