package org.ieatta.parse;

import com.parse.ParseObject;

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

import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;

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
        String whatToEat= object.getString(ParseObjectConstant.kPAPFieldWhatToEatKey);
        String remarks= object.getString(ParseObjectConstant.kPAPFieldRemarksKey);
        String waiter= object.getString(ParseObjectConstant.kPAPFieldWaiterKey);
        String restaurantRef= object.getString(ParseObjectConstant.kPAPFieldLocalRestaurantKey);


        model.setUUID(uuid);
        model.setObjectCreatedDate(objectCreatedDate);
        model.setDisplayName(displayName);
        model.setStartDate(startDate);
        model.setEndDate(endDate);
        model.setWhatToEat(whatToEat);
        model.setRemarks(remarks);
        model.setWaiter(waiter);
        model.setRestaurantRef(restaurantRef);

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

        return model;
    }

    public static DBPeopleInEvent reader(ParseObject object, DBPeopleInEvent model) {

        return model;
    }

    public static DBPhoto reader(ParseObject object, DBPhoto model) {
        return model;
    }

    public static DBRecipe reader(ParseObject object, DBRecipe model) {
        return model;
    }

    public static DBRestaurant reader(ParseObject object, DBRestaurant model) {
        return model;
    }

    public static DBReview reader(ParseObject object, DBReview model) {
        return model;
    }

    public static DBTeam reader(ParseObject object, DBTeam model) {
        return model;
    }
}
