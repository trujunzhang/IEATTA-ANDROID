package org.ieatta.analytics;

import org.ieatta.IEATTAApp;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.provide.ReviewType;
import org.wikipedia.analytics.Funnel;

import java.io.File;

public class PhotoFunnel extends Funnel {

    private static final String SCHEMA_NAME = "PhotoFunnel";
    private static final int REV_ID = 101123;

    public PhotoFunnel(IEATTAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public PhotoFunnel(){
        super(IEATTAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logDownloadThumbnail(String thumbnailUrl) {
         log("Download thumbnail url",thumbnailUrl);
    }

    public void logCacheThumbnail(File thumbnailFile) {
         log("Cached thumbnail path",thumbnailFile.getAbsolutePath());
    }
}
