package org.ieatta.database.query;

import android.location.Location;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.AppConstant;
import org.ieatta.utils.GeoHashUtil;

import java.util.LinkedList;
import java.util.List;

import bolts.Task;
import io.realm.Realm;
import io.realm.RealmResults;

public class LocalDatabaseQuery {

    public static Task<RealmResults<DBRestaurant>> queryNearRestaurants(Location location, List<Realm> realmList) {
        DBBuilder builder = new DBBuilder().whereContainedIn(AppConstant.kPAPFieldModelGEOHASH, GeoHashUtil.getEncodeHash(location));
//        DBBuilder builder = new DBBuilder();
        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).fetchResults(builder, false,realmList);
    }

    public static Task<RealmResults<DBPhoto>> queryPhotosForRestaurant(String UUID, List<Realm> realmList) {
        DBBuilder builder = new DBBuilder()
                .whereEqualTo(AppConstant.kPAPFieldLocalRestaurantKey, UUID)
                .orderByDescending(AppConstant.kPAPFieldObjectCreatedDateKey);
        return new RealmModelReader<DBPhoto>(DBPhoto.class).fetchResults(builder, true,realmList);
    }

    public static Task<RealmResults<DBPhoto>> queryPhotosByModel(String usedRef,int usedType, List<Realm> realmList) {
        DBBuilder builder = new DBBuilder()
                .whereEqualTo(AppConstant.kPAPFieldUsedRefKey, usedRef)
                .whereEqualTo(AppConstant.kPAPFieldUsedTypeKey, usedType)
                .orderByDescending(AppConstant.kPAPFieldObjectCreatedDateKey);
        return new RealmModelReader<DBPhoto>(DBPhoto.class).fetchResults(builder, false,realmList);
    }

    public static Task<RealmResults<DBPhoto>> getPhotos(String usedRef, List<Realm> realmList){
        DBBuilder builder = new DBBuilder()
                .whereEqualTo(AppConstant.kPAPFieldUsedRefKey, usedRef)
                .orderByDescending(AppConstant.kPAPFieldObjectCreatedDateKey);
        return new RealmModelReader<DBPhoto>(DBPhoto.class).fetchResults(builder, false,realmList);
    }

    public static Task<DBPhoto> getPhoto(String usedRef, boolean needClose, List<Realm> realmList){
        DBBuilder builder = new DBBuilder()
                .whereEqualTo(AppConstant.kPAPFieldUsedRefKey, usedRef)
                .orderByDescending(AppConstant.kPAPFieldObjectCreatedDateKey);
        return new RealmModelReader<DBPhoto>(DBPhoto.class).getFirstObject(builder, needClose, realmList);
    }

    public static DBBuilder get(String UUID) {
        return new DBBuilder().whereEqualTo(AppConstant.kPAPFieldObjectUUIDKey, UUID);
    }

    public static DBBuilder getQueryOrderedPeople(String eventUUID) {
        return new DBBuilder()
                .whereEqualTo(AppConstant.kPAPFieldEventKey, eventUUID)
                .orderByDescending(AppConstant.kPAPFieldObjectCreatedDateKey);
    }

    public static DBBuilder getObjectsByUUIDs(List<String> UUIDs) {
        return new DBBuilder()
                .whereContainedIn(AppConstant.kPAPFieldObjectUUIDKey, UUIDs)
                .orderByDescending(AppConstant.kPAPFieldObjectCreatedDateKey);
    }

    public static DBBuilder getForRecipes(String teamUUID,String eventUUID) {
        return new DBBuilder()
                .whereEqualTo(AppConstant.kPAPFieldOrderedPeopleRefKey, teamUUID)
                .whereEqualTo(AppConstant.kPAPFieldEventRefKey, eventUUID)
                .orderByDescending(AppConstant.kPAPFieldObjectCreatedDateKey);
    }


    public static List<Realm> closeRealmList(List<Realm> realmList) {
        for(Realm realm : realmList) {
            if (realm.isClosed() == false)
                realm.close();
        }
        return new LinkedList<>();
    }
}
