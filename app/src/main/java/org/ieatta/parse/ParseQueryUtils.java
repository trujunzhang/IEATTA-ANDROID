package org.ieatta.parse;

import com.parse.ParseQuery;

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
}
