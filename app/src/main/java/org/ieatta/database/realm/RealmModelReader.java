package org.ieatta.database.realm;

import org.ieatta.IEAApp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bolts.Task;
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

    public Task<RealmResults<T>> fetchResults(DBBuilder builder, boolean needClose){
        RealmResults<T> result = null;

        Realm realm = Realm.getInstance(IEAApp.getInstance());
        try {
            RealmQuery<T> query = realm.where(this.clazz);

            buildAll(builder, query);

            // Execute the query:
            result= query.findAll();

            resultSorted(builder, result);
        } catch (Exception e) {
            return Task.forError(e);
        } finally {
            if(needClose) {
                realm.close();
            }
        }

        return Task.forResult(result);
    }

    public Task<T> getFirstObject(DBBuilder builder, boolean needClose){
        T result = null;

        Realm realm = Realm.getInstance(IEAApp.getInstance());
        try {
            RealmQuery<T> query = realm.where(this.clazz);

            buildAll(builder, query);

            // Execute the query:
            result= query.findFirst();

        } catch (Exception e) {
            return Task.forError(e);
        } finally {
            if(needClose) {
                realm.close();
            }
        }

        return Task.forResult(result);
    }


    private void buildAll(DBBuilder builder, RealmQuery<T> query) {
        buildGreaterMap(builder, query);
        buildContainedMap(builder, query);
        buildContainedListMap(builder, query);
        buildEqualMap(builder, query);
    }

    private void buildContainedListMap(DBBuilder builder, RealmQuery<T> query) {
        HashMap<String, List<String>> containedListMap = builder.containedListMap;
        if(containedListMap.keySet().size() == 0)
            return;

        for(String key : containedListMap.keySet()) {
            List<String> list = containedListMap.get(key);
            RealmQuery<T> beginGroup = query.beginGroup();
            for(String value: list){
                beginGroup.equalTo(key,value).or();
            }
            query.endGroup();
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
        HashMap<String, Object> equalMap = builder.equalMap;
        if(equalMap.keySet().size() == 0)
            return;

        for(String key : equalMap.keySet()) {
            Object value = equalMap.get(key);
            if (value instanceof String) {
                query.equalTo(key, (String)value);
            }else if (value instanceof Integer) {
                query.equalTo(key, (int)value);
            }
        }
    }

    private void buildContainedMap(DBBuilder builder, RealmQuery<T> query) {
        HashMap<String, String> containedMap = builder.containedMap;
        if(containedMap.keySet().size() == 0)
            return;

        for(String key : containedMap.keySet()) {
            query.contains(key, containedMap.get(key));
        }
    }

    private void buildGreaterMap(DBBuilder builder, RealmQuery<T> query) {
        HashMap<String, Object> greaterMap = builder.greaterMap;
        if(greaterMap.keySet().size() == 0)
            return;

        for(String key : greaterMap.keySet()) {
            Object value = greaterMap.get(key);
            if (value instanceof Date) {
                query.greaterThan(key, (Date) value);
            }else if (value instanceof Integer) {
                query.greaterThan(key, (int) value);
            }
        }
    }

}
