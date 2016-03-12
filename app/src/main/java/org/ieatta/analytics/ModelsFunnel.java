package org.ieatta.analytics;

import org.ieatta.IEATTAApp;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.provide.ReviewType;
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

    public ModelsFunnel(){
        super(IEATTAApp.getInstance(),SCHEMA_NAME,REV_ID,SAMPLE_LOG_1K);
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
        log("info",log);
    }

    public void logNewRecord(DBNewRecord newRecord) {
        String log = "NewRecord{" +
                "UUID='" + newRecord.getUUID() + '\'' +
                ", modelType=" + PQueryModelType.getInstance(newRecord.getModelType()).toString() +
                ", modelPoint='" + newRecord.getModelPoint() + '\'' +
                '}';
        log("info",log);
    }

    public void logPeopleInEvent(DBPeopleInEvent peopleInEvent) {
        String log = "PeopleInEvent{" +
                "UUID='" + peopleInEvent.getUUID() + '\'' +
                ", userRef='" + peopleInEvent.getUserRef() + '\'' +
                ", eventRef='" + peopleInEvent.getEventRef() + '\'' +
                '}';
        log("info",log);
    }

    public void logPhoto(DBPhoto photo) {
        String log = "Photo{" +
                "UUID='" + photo.getUUID() + '\'' +
                ", usedRef='" + photo.getUsedRef() + '\'' +
                ", restaurantRef='" + photo.getRestaurantRef() + '\'' +
                ", thumbnailUrl='" + photo.getThumbnailUrl() + '\'' +
                ", originalUrl='" + photo.getOriginalUrl() + '\'' +
                ", usedType=" + PhotoUsedType.getInstance(photo.getUsedType()).toString() +
                '}';
        log("info",log);
    }

    public void logRecipe(DBRecipe recipe) {
        String log = "Recipe{" +
                "UUID='" + recipe.getUUID() + '\'' +
                ", price=" + recipe.getPrice() +
                ", eventRef='" + recipe.getEventRef() + '\'' +
                ", orderedPeopleRef='" + recipe.getOrderedPeopleRef() + '\'' +
                '}';
        log("info",log);
    }

    public void logRestaurant(DBRestaurant restaurant) {
        String log = "Restaurant{" +
                "UUID='" + restaurant.getUUID() + '\'' +
                ", displayName='" + restaurant.getDisplayName() + '\'' +
//                ", location=" + restaurant +
                ", googleMapAddress='" + restaurant.getGoogleMapAddress() + '\'' +
                '}';
        log("info",log);
    }

    public void logReview(DBReview review) {
        String log = "Review{" +
                "UUID='" + review.getUUID() + '\'' +
                ", content='" + review.getContent() + '\'' +
                ", rate=" + review.getRate() +
                ", userRef='" + review.getUserRef() + '\'' +
                ", reviewRef='" + review.getReviewRef() + '\'' +
                ", reviewType=" + ReviewType.getInstance(review.getReviewType()).toString() +
                '}';
        log("info",log);
    }

    public void logTeam(DBTeam team) {
        String log = "Team{" +
                "UUID='" + team.getUUID() + '\'' +
                ", displayName='" + team.getDisplayName() + '\'' +
                ", email='" + team.getEmail() + '\'' +
                ", address='" + team.getAddress() + '\'' +
                '}';
        log("info",log);
    }

}
