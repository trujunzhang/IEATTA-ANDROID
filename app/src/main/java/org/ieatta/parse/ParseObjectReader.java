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

import java.util.Date;

public class ParseObjectReader {

    public static void reader(ParseObject object,DBEvent event){

    }

    public static void reader(ParseObject object,DBNewRecord newRecord){
        String uuid = object.getString(ParseObjectConstant.kPAPFieldObjectUUIDKey);
        Date objectCreatedDate = object.getDate(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        int type = object.getInt(ParseObjectConstant.kPAPFieldModelTypeKey);
        String modelPoint = object.getString(ParseObjectConstant.kPAPFieldModelPointKey);

        newRecord.setUUID(uuid);
        newRecord.setObjectCreatedDate(objectCreatedDate);
        newRecord.setModelType(type);
        newRecord.setModelPoint(modelPoint);
    }

    public static void reader(ParseObject object,DBPeopleInEvent peopleInEvent){

    }

    public static void reader(ParseObject object,DBPhoto photo){

    }

    public static void reader(ParseObject object,DBRecipe recipe){

    }

    public static void reader(ParseObject object,DBRestaurant restaurant){

    }

    public static void reader(ParseObject object,DBReview review){

    }

    public static void reader(ParseObject object,DBTeam team){

    }
}
