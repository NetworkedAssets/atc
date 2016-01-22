package com.networkedassets.plugins.space_blueprint;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.plugins.createcontent.api.events.SpaceBlueprintCreateEvent;
import com.atlassian.confluence.plugins.createcontent.impl.SpaceBlueprint;
import com.atlassian.confluence.spaces.Space;
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
 * Created by mrobakowski on 1/22/2016.
 */
@SuppressWarnings("unused")
public class Arc42CreatedListener {
    private static final ModuleCompleteKey MY_BLUEPRINT_KEY =
            new ModuleCompleteKey("com.networkedassets.plugins.space-blueprint", "space-blueprint");
    private static final Logger log = LoggerFactory.getLogger(Arc42CreatedListener.class);
    private final AttachmentManager attachmentManager;

    public Arc42CreatedListener(AttachmentManager attachmentManager, EventPublisher eventPublisher) {
        this.attachmentManager = attachmentManager;
        eventPublisher.register(this);
    }

    @EventListener
    public void handleArc42SpaceCreated(SpaceBlueprintCreateEvent e) throws URISyntaxException, IOException {
        Space space = e.getSpace();
        SpaceBlueprint spaceBlueprint = e.getSpaceBlueprint();
        if (spaceBlueprint.getModuleCompleteKey().equals(MY_BLUEPRINT_KEY.getCompleteKey())) {
            InputStream logoStream = Arc42CreatedListener.class.getClassLoader().getResourceAsStream("images/logo_arc42.png");

            Attachment logo = new Attachment(); // TODO: fix npe caused by not loading resource
            logo.setFileSize(6089);
            logo.setFileName(space.getKey());

            attachmentManager.saveAttachment(logo, null, logoStream);
            log.error("STUFF'S DONE");
        }
    }

}
