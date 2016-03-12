package org.ieatta.parse;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;

import java.util.Date;

public class ParseQueryUtils {

    public static ParseQuery createQueryForNewRecord(Date lastAsyncDate, int limit) {
        ParseQuery query = ParseQuery.getQuery(PQueryModelType.NewRecord.toString());
        query.setLimit(limit);

        // *** Important (used orderByAscending) ***
        query.orderByAscending(ParseObjectConstant.kPAPFieldModelOnlineCreatedAtKey);

        if (lastAsyncDate != null)
            query.whereGreaterThan(ParseObjectConstant.kPAPFieldModelOnlineCreatedAtKey, lastAsyncDate);

        return query;
    }

    public static ParseQuery createQueryForRecorded(ParseObject newRecordObject) {
        DBNewRecord newRecord = new DBNewRecord();
        ParseObjectReader.reader(newRecordObject, newRecord);

        PQueryModelType modelType = PQueryModelType.getInstance(newRecord.getModelType());
        ParseQuery query = ParseQuery.getQuery(modelType.toString());


        return query;
    }
}
