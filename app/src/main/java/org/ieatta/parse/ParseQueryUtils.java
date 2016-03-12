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

    public static ParseQuery<ParseObject> createQueryForRecorded(DBNewRecord newRecord) {
        PQueryModelType type = PQueryModelType.getInstance(newRecord.getModelType());

        ParseQuery<ParseObject> query = ParseQuery.getQuery(type.toString());

        // *** Import *** The newest row in the table.
        query.orderByDescending(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        query.whereEqualTo(ParseObjectConstant.kPAPFieldObjectUUIDKey, newRecord.getModelPoint());

        return query;
    }
}
