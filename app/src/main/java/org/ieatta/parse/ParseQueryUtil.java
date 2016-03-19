package org.ieatta.parse;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;

import java.util.Date;

public class ParseQueryUtil {

    public static ParseQuery createQueryForNewRecord(Date lastAsyncDate, int limit) {
        ParseQuery query = ParseQuery.getQuery(PQueryModelType.NewRecord.toString());
        query.setLimit(limit);

        // *** Important (used orderByAscending) ***
        query.orderByAscending(DBConstant.kPAPFieldModelOnlineCreatedAtKey);

        if (lastAsyncDate != null)
            query.whereGreaterThan(DBConstant.kPAPFieldModelOnlineCreatedAtKey, lastAsyncDate);

        return query;
    }

    public static ParseQuery<ParseObject> createQueryForRecorded(DBNewRecord newRecord) {
        PQueryModelType type = PQueryModelType.getInstance(newRecord.getModelType());

        ParseQuery<ParseObject> query = ParseQuery.getQuery(type.toString());

        // *** Import *** The newest row in the table.
        query.orderByDescending(DBConstant.kPAPFieldObjectCreatedDateKey);
        query.whereEqualTo(DBConstant.kPAPFieldObjectUUIDKey, newRecord.getModelPoint());

        return query;
    }

    /**
     *
     * @param uuid  Model's UUID
     * @return
     */
    public static ParseQuery<ParseObject> createQueryByUUID(String uuid, PQueryModelType type) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(type.toString());

        query.whereEqualTo(DBConstant.kPAPFieldObjectUUIDKey, uuid);

        return query;
    }
}
