package org.ieatta.database.realm;

import org.ieatta.IEATTAApp;
import org.ieatta.database.models.DBRestaurant;

import java.lang.reflect.ParameterizedType;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmModelReader<T extends RealmObject> {
    private Class<T > clazz;

    public RealmModelReader(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void fetchResults(DBBuilder builder){
        Realm realm = Realm.getInstance(IEATTAApp.getInstance());

        RealmQuery<T> query = realm.where(this.clazz);

        for(String key :builder.greaterMap.keySet()) {
            Object value = builder.greaterMap.get(key);
            if (value instanceof Date) {
                query.greaterThan(key, (Date) value);
            }else if (value instanceof Integer) {
                query.greaterThan(key, (int) value);
            }
        }

        for(String key :builder.containedMap.keySet()) {
            query.contains(key,builder.containedMap.get(key));
        }

        for(String key :builder.equalMap.keySet()) {
            Object value = builder.equalMap.get(key);
            if (value instanceof String) {
                query.equalTo(key, (String)value);
            }else if (value instanceof Integer) {
                query.equalTo(key, (int)value);
            }
        }

        // Execute the query:
        RealmResults<T> result= query.findAll();

        int size = result.size();
    }

}
