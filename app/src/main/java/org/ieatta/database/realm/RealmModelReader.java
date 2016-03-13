package org.ieatta.database.realm;

import org.ieatta.IEATTAApp;
import org.ieatta.database.models.DBRestaurant;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmModelReader<T extends RealmObject> {

    public void fetchRestaurants(){
        Realm realm = Realm.getInstance(IEATTAApp.getInstance());
        RealmQuery<DBRestaurant> query = realm.where(DBRestaurant.class);

        // Execute the query:
        RealmResults<DBRestaurant> result= query.findAll();

        int size = result.size();
    }

}
