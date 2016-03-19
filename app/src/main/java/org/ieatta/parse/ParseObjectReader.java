package org.ieatta.parse;

import com.github.davidmoten.geo.GeoHash;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.ieatta.analytics.ModelsFunnel;
import org.ieatta.analytics.PhotoFunnel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.InputStream;
import java.util.Date;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmObject;

public class ParseObjectReader {

    public Task<RealmObject> read(ParseObject object, PQueryModelType type) {
        switch (type) {
            case Recipe:
                return reader(object, new DBRecipe());
            case Photo:
                return reader(object, new DBPhoto());
            case Team:
                return reader(object, new DBTeam());
            case Review:
                return reader(object, new DBReview());
            case Event:
                return reader(object, new DBEvent());
            case Restaurant:
                return reader(object, new DBRestaurant());
            //case NewRecord:
            //    return reader(object, new DBNewRecord());
            case PeopleInEvent:
                return reader(object, new DBPeopleInEvent());
            default:
                break;
        }
        return Task.forError(new NullPointerException("unknown model type."));
    }

    public Task<RealmObject> reader(ParseObject object, DBEvent model) {
        String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(DBConstant.kPAPFieldDisplayNameKey);
        Date startDate = object.getDate(DBConstant.kPAPFieldStartDateKey);
        Date endDate = object.getDate(DBConstant.kPAPFieldEndDateKey);
        String whatToEat = object.getString(DBConstant.kPAPFieldWhatToEatKey);
        String remarks = object.getString(DBConstant.kPAPFieldRemarksKey);
        String waiter = object.getString(DBConstant.kPAPFieldWaiterKey);
        String restaurantRef = object.getString(DBConstant.kPAPFieldLocalRestaurantKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setStartDate(startDate);
        model.setEndDate(endDate);
        model.setWhatToEat(whatToEat);
        model.setRemarks(remarks);
        model.setWaiter(waiter);
        model.setRestaurantRef(restaurantRef);

        new ModelsFunnel().logEvent(model);
        return Task.forResult((RealmObject) model);
    }

    public DBNewRecord reader(ParseObject object, DBNewRecord model) {
        String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);
        int type = object.getInt(DBConstant.kPAPFieldModelTypeKey);
        String modelPoint = object.getString(DBConstant.kPAPFieldModelPointKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setModelType(type);
        model.setModelPoint(modelPoint);

        new ModelsFunnel().logNewRecord(model);
        return model;
    }

    public Task<RealmObject> reader(ParseObject object, DBPeopleInEvent model) {
        String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);
        String userRef = object.getString(DBConstant.kPAPFieldUserKey);
        String eventRef = object.getString(DBConstant.kPAPFieldEventKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setUserRef(userRef);
        model.setEventRef(eventRef);

        new ModelsFunnel().logPeopleInEvent(model);
        return Task.forResult((RealmObject) model);
    }

    public Task<RealmObject> reader(ParseObject object, final DBPhoto model) {
        final String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);

        ParseFile originalFile = object.getParseFile(DBConstant.kPAPFieldOriginalImageKey);
        ParseFile thumbnailFile = object.getParseFile(DBConstant.kPAPFieldThumbnailImageKey);
        String originalUrl = originalFile.getUrl();
        final String thumbnailUrl = thumbnailFile.getUrl();

        String usedRef = object.getString(DBConstant.kPAPFieldUsedRefKey);
        int usedType = object.getInt(DBConstant.kPAPFieldUsedTypeKey);
        String restaurantRef = object.getString(DBConstant.kPAPFieldLocalRestaurantKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setOriginalUrl(originalUrl);
        model.setThumbnailUrl(thumbnailUrl);
        model.setUsedRef(usedRef);
        model.setUsedType(usedType);
        model.setRestaurantRef(restaurantRef);

        new ModelsFunnel().logPhoto(model);

        return thumbnailFile.getDataStreamInBackground().onSuccessTask(new Continuation<InputStream, Task<Void>>() {
            @Override
            public Task<Void> then(Task<InputStream> task) throws Exception {
                new PhotoFunnel().logDownloadThumbnail(thumbnailUrl);
                return ThumbnailImageUtil.sharedInstance.saveTakenPhoto(task.getResult(),model);
            }
        }).onSuccessTask(new Continuation<Void, Task<RealmObject>>() {
            @Override
            public Task<RealmObject> then(Task<Void> task) throws Exception {
                // new PhotoFunnel().logCacheThumbnail(ThumbnailImageUtil.sharedInstance.getTakenPhotoFile(uuid));
                return Task.forResult((RealmObject) model);
            }
        });
    }

    public Task<RealmObject> reader(ParseObject object, DBRecipe model) {
        String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(DBConstant.kPAPFieldDisplayNameKey);
        String orderedPeopleRef = object.getString(DBConstant.kPAPFieldOrderedPeopleRefKey);
        String price = object.getString(DBConstant.kPAPFieldPriceKey);
        String eventRef = object.getString(DBConstant.kPAPFieldEventRefKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setOrderedPeopleRef(orderedPeopleRef);
        model.setPrice(price);
        model.setEventRef(eventRef);

        new ModelsFunnel().logRecipe(model);
        return Task.forResult((RealmObject) model);
    }

    /**
     * Read variables from the ParseObject to realm object.
     * Generate 'Geohash' for the restaurant to implement nearby search.
     * @param object    : ParseObject's instance
     * @param model     : new Instance of Realm Object
     * @return
     */
    public Task<RealmObject> reader(ParseObject object, DBRestaurant model) {
        String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(DBConstant.kPAPFieldDisplayNameKey);
        String googleMapAddress = object.getString(DBConstant.kPAPFieldAddressKey);
        ParseGeoPoint geoPoint = object.getParseGeoPoint(DBConstant.kPAPFieldLocationKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setGoogleMapAddress(googleMapAddress);
        model.setLatitude(geoPoint.getLatitude());
        model.setLongitude(geoPoint.getLongitude());

        String geoHash = GeoHash.encodeHash(geoPoint.getLatitude(),geoPoint.getLongitude());
        model.setGeoHash(geoHash);

        new ModelsFunnel().logRestaurant(model);
        return Task.forResult((RealmObject) model);
    }

    public Task<RealmObject> reader(ParseObject object, DBReview model) {
        String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);
        int rate = object.getInt(DBConstant.kPAPFieldRateKey);
        int reviewType = object.getInt(DBConstant.kPAPFieldReviewTypeKey);
        String reviewRef = object.getString(DBConstant.kPAPFieldReviewRefKey);
        String userRef = object.getString(DBConstant.kPAPFieldUserRefKey);
        String content = object.getString(DBConstant.kPAPFieldContentKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setRate(rate);
        model.setReviewRef(reviewRef);
        model.setReviewType(reviewType);
        model.setUserRef(userRef);
        model.setContent(content);

        new ModelsFunnel().logReview(model);
        return Task.forResult((RealmObject) model);
    }

    public Task<RealmObject> reader(ParseObject object, DBTeam model) {
        String uuid = object.getString(DBConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(DBConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(DBConstant.kPAPFieldDisplayNameKey);
        String email = object.getString(DBConstant.kPAPFieldEmailKey);
        String address = object.getString(DBConstant.kPAPFieldAddressKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setEmail(email);
        model.setAddress(address);

        new ModelsFunnel().logTeam(model);
        return Task.forResult((RealmObject) model);
    }
}
