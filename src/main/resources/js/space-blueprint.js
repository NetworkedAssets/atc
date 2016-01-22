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
 * This script hooks into the wizard API. It adds the blueprint to the wizard and updates the space home page title to be the space name 
 * followed by the suffix "Home Page".
 *
 *  @author Tina Steiger  
 */

AJS.bind("blueprint.wizard-register.ready", function () {
    function submitArcSpace(e, state) {
        state.pageData.ContentPageTitle = state.pageData.name + " Home Page";
        console.log(Object.keys(state.pageData));
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