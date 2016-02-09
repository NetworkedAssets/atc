package com.networkedassets.plugins.space_blueprint;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.plugins.createcontent.api.events.SpaceBlueprintCreateEvent;
import com.atlassian.confluence.plugins.createcontent.impl.SpaceBlueprint;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourScheme;
import com.atlassian.confluence.themes.ColourSchemeManager;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.ModuleCompleteKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Event listener that changes the arc42 space logo after creation
 */
@SuppressWarnings("unused")
public class Arc42CreatedListener {
    private static final ModuleCompleteKey MY_BLUEPRINT_KEY =
            new ModuleCompleteKey("com.networkedassets.plugins.space-blueprint", "space-blueprint");
    private static final Logger log = LoggerFactory.getLogger(Arc42CreatedListener.class);
    private final AttachmentManager attachmentManager;
    private final ColourSchemeManager colourSchemeManager;

    public Arc42CreatedListener(AttachmentManager attachmentManager, EventPublisher eventPublisher, ColourSchemeManager colourSchemeManager) {
        this.attachmentManager = attachmentManager;
        this.colourSchemeManager = colourSchemeManager;
        eventPublisher.register(this);
    }

    @EventListener
    public void handleSpaceBlueprintCreate(SpaceBlueprintCreateEvent e) throws URISyntaxException, IOException {
        Space space = e.getSpace();
        SpaceBlueprint spaceBlueprint = e.getSpaceBlueprint();
        if (spaceBlueprint.getModuleCompleteKey().equals(MY_BLUEPRINT_KEY.getCompleteKey())) {
            handleArc42Create(space);
        }
    }

    private void handleArc42Create(Space arc42Space) throws IOException {
        setSpaceIcon(arc42Space);
        setCustomColorScheme(arc42Space);
    }

    private void setCustomColorScheme(Space space) {
        final BaseColourScheme colourScheme = colourSchemeManager.getSpaceColourSchemeIsolated(space.getKey());
        colourScheme.set(ColourScheme.TOP_BAR, "#10803F");
        colourScheme.set(ColourScheme.BREADCRUMBS_TEXT, "#FFFFFF");
        colourScheme.set(ColourScheme.HEADER_BUTTON_BASE_BACKGROUND, "#C6D92D");
        colourScheme.set(ColourScheme.HEADER_BUTTON_TEXT, "#FFFFFF");
        colourScheme.set(ColourScheme.TOP_BAR_MENU_SELECTED_BACKGROUND, "#1D803F");
        colourScheme.set(ColourScheme.TOP_BAR_MENU_SELECTED_TEXT, "#FFFFFF");
        colourScheme.set(ColourScheme.TOP_BAR_MENU_ITEM_TEXT, "#333333");
        colourScheme.set(ColourScheme.MENU_ITEM_SELECTED_BACKGROUND, "#1D803F");
        colourScheme.set(ColourScheme.MENU_ITEM_SELECTED_TEXT, "#FFFFFF");
        colourScheme.set(ColourScheme.SEARCH_FIELD_BACKGROUND, "#FFFFFF");
        colourScheme.set(ColourScheme.SEARCH_FIELD_TEXT, "#FFFFFF");
        colourScheme.set(ColourScheme.MENU_SELECTED_BACKGROUND, "#1D803F");
        colourScheme.set(ColourScheme.MENU_ITEM_TEXT, "#333333");
        colourScheme.set(ColourScheme.HEADING_TEXT, "#333333");
        colourScheme.set(ColourScheme.SPACE_NAME, "#CCCCCC");
        colourScheme.set(ColourScheme.LINK, "#1D803F");
        colourScheme.set(ColourScheme.BORDER, "#CCCCCC");
        colourScheme.set(ColourScheme.NAV_BACKGROUND, "#1D803F");
        colourScheme.set(ColourScheme.NAV_TEXT, "#FFFFFF");
        colourScheme.set(ColourScheme.NAV_SELECTED_BACKGROUND, "#060920");
        colourScheme.set(ColourScheme.NAV_SELECTED_TEXT, "#FFFFFF");
        colourSchemeManager.saveSpaceColourScheme(space, colourScheme);
    }

    private void setSpaceIcon(Space space) throws IOException {
        InputStream logoStream = Arc42CreatedListener.class.getClassLoader().getResourceAsStream("images/logo_arc42_space.png");

        Attachment logo = new Attachment();
        logo.setContainer(space.getDescription());
        logo.setMediaType("image/png");
        logo.setFileSize(4848); // TODO: this shouldn't be hardcoded
        logo.setFileName(space.getKey());

        attachmentManager.saveAttachment(logo, null, logoStream);
    }

}
