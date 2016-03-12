package org.ieatta.analytics;

import org.ieatta.IEATTAApp;
import org.ieatta.database.provide.PQueryModelType;
import org.wikipedia.analytics.Funnel;

import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;

public class ModelsFunnel extends Funnel {

    private static final String SCHEMA_NAME = "MobileIEATTAModels";
    private static final int REV_ID = 101010;

    public ModelsFunnel(IEATTAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logEvent(DBEvent event) {
        String log = "Event{" +
                "UUID='" + event.getUUID() + '\'' +
                ", restaurantRef='" + event.getRestaurantRef() + '\'' +
                ", displayName='" + event.getDisplayName() + '\'' +
                ", startDate=" + event.getStartDate() +
                ", endDate=" + event.getEndDate() +
                ", whatToEat='" + event.getWhatToEat() + '\'' +
                ", remarks='" + event.getRemarks() + '\'' +
                ", waiter='" + event.getWaiter() + '\'' +
                '}';
        log(log);
    }

    public void logNewRecord(DBNewRecord newRecord) {
        String log = "NewRecord{" +
                "UUID='" + newRecord.getUUID() + '\'' +
                ", modelType=" + PQueryModelType.getInstance(newRecord.getModelType()).toString() +
                ", modelPoint='" + newRecord.getModelPoint() + '\'' +
                '}';
        log(log);
    }

    public void logPeopleInEvent(DBPeopleInEvent peopleInEvent) {

    }

    public void logPhoto(DBPhoto photo) {

    }

    public void logRecipe(DBRecipe recipe) {

    }

    public void logRestaurant(DBRestaurant restaurant) {

    }

    public void logReview(DBReview review) {

    }

    public void logTeam(DBTeam team) {

    }

}
