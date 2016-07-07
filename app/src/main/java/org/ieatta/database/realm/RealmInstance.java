package org.ieatta.database.realm;

import org.ieatta.IEAApp;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmInstance {
    public static Realm getInstance() {
        RealmConfiguration config = new RealmConfiguration
                .Builder(IEAApp.getInstance())
                .build();
        return Realm.getInstance(config);
    }
}
