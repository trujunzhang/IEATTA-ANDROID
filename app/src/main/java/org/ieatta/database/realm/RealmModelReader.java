package org.ieatta.database.realm;

import org.ieatta.IEATTAApp;
import org.ieatta.database.models.DBRestaurant;

import java.lang.reflect.ParameterizedType;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmModelReader<T extends RealmObject> {
    private Class<T > clazz;

    public RealmModelReader(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void fetchRestaurants(){
        Realm realm = Realm.getInstance(IEATTAApp.getInstance());

        RealmQuery<T> query = realm.where(this.clazz);

        // Execute the query:
        RealmResults<T> result= query.findAll();

        int size = result.size();
    }

}
