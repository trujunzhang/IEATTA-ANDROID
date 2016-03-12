package org.ieatta.parse;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.ieatta.analytics.ModelsFunnel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PQueryModelType;

import java.util.Date;
import io.realm.RealmObject;

public class ParseObjectReader {

    public static RealmObject read(ParseObject object, PQueryModelType type) {
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
            case NewRecord:
                return reader(object, new DBNewRecord());
            case PeopleInEvent:
                return reader(object, new DBPeopleInEvent());
            default:
                break;
        }
        return null;
    }

    public static DBEvent reader(ParseObject object, DBEvent model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(ParseObjectConstant.kPAPFieldDisplayNameKey);
        Date startDate = object.getDate(ParseObjectConstant.kPAPFieldStartDateKey);
        Date endDate = object.getDate(ParseObjectConstant.kPAPFieldEndDateKey);
        String whatToEat = object.getString(ParseObjectConstant.kPAPFieldWhatToEatKey);
        String remarks = object.getString(ParseObjectConstant.kPAPFieldRemarksKey);
        String waiter = object.getString(ParseObjectConstant.kPAPFieldWaiterKey);
        String restaurantRef = object.getString(ParseObjectConstant.kPAPFieldLocalRestaurantKey);

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
        return model;
    }

    public static DBNewRecord reader(ParseObject object, DBNewRecord model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        int type = object.getInt(ParseObjectConstant.kPAPFieldModelTypeKey);
        String modelPoint = object.getString(ParseObjectConstant.kPAPFieldModelPointKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setModelType(type);
        model.setModelPoint(modelPoint);

        new ModelsFunnel().logNewRecord(model);
        return model;
    }

    public static DBPeopleInEvent reader(ParseObject object, DBPeopleInEvent model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        String userRef = object.getString(ParseObjectConstant.kPAPFieldUserKey);
        String eventRef = object.getString(ParseObjectConstant.kPAPFieldEventKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setUserRef(userRef);
        model.setEventRef(eventRef);

        new ModelsFunnel().logPeopleInEvent(model);
        return model;
    }

    public static DBPhoto reader(ParseObject object, DBPhoto model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        String originalUrl = object.getString(ParseObjectConstant.kPAPFieldOriginalUrlKey);
        String thumbnailUrl = object.getString(ParseObjectConstant.kPAPFieldThumbnailImageKey);
        String usedRef = object.getString(ParseObjectConstant.kPAPFieldUsedRefKey);
        int usedType = object.getInt(ParseObjectConstant.kPAPFieldUsedTypeKey);
        String restaurantRef = object.getString(ParseObjectConstant.kPAPFieldLocalRestaurantKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setOriginalUrl(originalUrl);
        model.setThumbnailUrl(thumbnailUrl);
        model.setUsedRef(usedRef);
        model.setUsedType(usedType);
        model.setRestaurantRef(restaurantRef);

        new ModelsFunnel().logPhoto(model);
        return model;
    }

    public static DBRecipe reader(ParseObject object, DBRecipe model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(ParseObjectConstant.kPAPFieldDisplayNameKey);
        String orderedPeopleRef = object.getString(ParseObjectConstant.kPAPFieldOrderedPeopleRefKey);
        String price = object.getString(ParseObjectConstant.kPAPFieldPriceKey);
        String eventRef = object.getString(ParseObjectConstant.kPAPFieldEventRefKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setOrderedPeopleRef(orderedPeopleRef);
        model.setPrice(price);
        model.setEventRef(eventRef);

        new ModelsFunnel().logRecipe(model);
        return model;
    }

    public static DBRestaurant reader(ParseObject object, DBRestaurant model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(ParseObjectConstant.kPAPFieldDisplayNameKey);
        String googleMapAddress = object.getString(ParseObjectConstant.kPAPFieldAddressKey);
        ParseGeoPoint geoPoint = object.getParseGeoPoint(ParseObjectConstant.kPAPFieldLocationKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setGoogleMapAddress(googleMapAddress);
        model.setLatitude(geoPoint.getLatitude());
        model.setLongitude(geoPoint.getLongitude());

        new ModelsFunnel().logRestaurant(model);
        return model;
    }

    public static DBReview reader(ParseObject object, DBReview model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        int rate = object.getInt(ParseObjectConstant.kPAPFieldRateKey);
        int reviewType = object.getInt(ParseObjectConstant.kPAPFieldReviewTypeKey);
        String reviewRef = object.getString(ParseObjectConstant.kPAPFieldReviewRefKey);
        String userRef = object.getString(ParseObjectConstant.kPAPFieldUserRefKey);
        String content = object.getString(ParseObjectConstant.kPAPFieldContentKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setRate(rate);
        model.setReviewRef(reviewRef);
        model.setReviewType(reviewType);
        model.setUserRef(userRef);
        model.setContent(content);

        new ModelsFunnel().logReview(model);
        return model;
    }

    public static DBTeam reader(ParseObject object, DBTeam model) {
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        String displayName = object.getString(ParseObjectConstant.kPAPFieldDisplayNameKey);
        String email = object.getString(ParseObjectConstant.kPAPFieldEmailKey);
        String address = object.getString(ParseObjectConstant.kPAPFieldAddressKey);

        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setEmail(email);
        model.setAddress(address);

        new ModelsFunnel().logTeam(model);
        return model;
    }
}
