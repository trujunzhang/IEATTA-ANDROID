package org.ieatta.database.realm;

import org.ieatta.IEATTAApp;
import org.ieatta.database.models.DBRestaurant;

import java.lang.reflect.ParameterizedType;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmModelReader<T extends RealmObject> {
    private Class<T > clazz;

    public RealmModelReader(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void fetchResults(DBBuilder builder){
        Realm realm = Realm.getInstance(IEATTAApp.getInstance());
        RealmResults<T> result = null;
        try {
            RealmQuery<T> query = realm.where(this.clazz);

            buildGreaterMap(builder, query);

            buildContainedMap(builder, query);

            buildEqualMap(builder, query);

            // Execute the query:
            result= query.findAll();

            resultSorted(builder, result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }

    }

    private void resultSorted(DBBuilder builder, RealmResults<T> result) {
        for(String value :builder.orderedByAscendingList) {
            result.sort(value, Sort.ASCENDING);
        }
        for(String value :builder.orderedByDescendingList) {
            result.sort(value,Sort.DESCENDING);
        }
    }

    private void buildEqualMap(DBBuilder builder, RealmQuery<T> query) {
        for(String key :builder.equalMap.keySet()) {
            Object value = builder.equalMap.get(key);
            if (value instanceof String) {
                query.equalTo(key, (String)value);
            }else if (value instanceof Integer) {
                query.equalTo(key, (int)value);
            }
        }
    }

    private void buildContainedMap(DBBuilder builder, RealmQuery<T> query) {
        for(String key :builder.containedMap.keySet()) {
            query.contains(key,builder.containedMap.get(key));
        }
    }

    private void buildGreaterMap(DBBuilder builder, RealmQuery<T> query) {
        for(String key :builder.greaterMap.keySet()) {
            Object value = builder.greaterMap.get(key);
            if (value instanceof Date) {
                query.greaterThan(key, (Date) value);
            }else if (value instanceof Integer) {
                query.greaterThan(key, (int) value);
            }
        }
    }

}
