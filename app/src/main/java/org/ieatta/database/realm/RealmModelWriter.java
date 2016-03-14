package org.ieatta.database.realm;

import org.ieatta.IEAApp;
import org.ieatta.database.provide.PQueryModelType;

import bolts.Task;
import io.realm.Realm;
import io.realm.RealmObject;

import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;

public class RealmModelWriter<T extends RealmObject> {

    public Task<Void> save(T model,PQueryModelType type) {
        // Obtain a Realm instance
        Realm realm = Realm.getInstance(IEAApp.getInstance());

        try {
            realm.beginTransaction();

            // add or update objects here ...
            realm.copyToRealmOrUpdate(model);

            realm.commitTransaction();
        } catch (Exception e) {
            return Task.forError(e);
        } finally {
            realm.close();
        }

        return Task.forResult(null);
    }

    private void write(Realm realm, T model,PQueryModelType type) {
        switch (type) {
            case Recipe:
                write((DBRecipe) model, realm);
                break;
            case Photo:
                write((DBPhoto) model, realm);
                break;
            case Team:
                write((DBTeam) model, realm);
                break;
            case Review:
                write((DBReview) model, realm);
                break;
            case Event:
                write((DBEvent) model, realm);
                break;
            case Restaurant:
                write((DBRestaurant) model, realm);
                break;
            case NewRecord:
                write((DBNewRecord) model, realm);
                break;
            case PeopleInEvent:
                write((DBPeopleInEvent) model, realm);
                break;
            default:
                break;
        }
    }

    public void write(DBEvent model, Realm realm) {
        DBEvent object = realm.createObject(DBEvent.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());
        object.setDisplayName(model.getDisplayName());

        object.setStartDate(model.getStartDate());
        object.setEndDate(model.getEndDate());
        object.setWhatToEat(model.getWhatToEat());
        object.setRemarks(model.getRemarks());
        object.setWaiter(model.getWaiter());
        object.setRestaurantRef(model.getRestaurantRef());
    }

    public void write(DBNewRecord model, Realm realm) {
        DBNewRecord object = realm.createObject(DBNewRecord.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());

        object.setModelType(model.getModelType());
        object.setModelPoint(model.getModelPoint());
    }

    public void write(DBPeopleInEvent model, Realm realm) {
        DBPeopleInEvent object = realm.createObject(DBPeopleInEvent.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());

        object.setUserRef(model.getUserRef());
        object.setEventRef(model.getEventRef());
    }

    public void write(DBPhoto model, Realm realm) {
        DBPhoto object = realm.createObject(DBPhoto.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());

        object.setRestaurantRef(model.getRestaurantRef());
        object.setUsedRef(model.getUsedRef());
        object.setUsedType(model.getUsedType());

        object.setOriginalUrl(model.getOriginalUrl());
        object.setThumbnailUrl(model.getThumbnailUrl());
    }

    public void write(DBRecipe model, Realm realm) {
        DBRecipe object = realm.createObject(DBRecipe.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());
        object.setDisplayName(model.getDisplayName());

        object.setPrice(model.getPrice());
        object.setEventRef(model.getEventRef());
        object.setOrderedPeopleRef(model.getOrderedPeopleRef());
    }

    public void write(DBRestaurant model, Realm realm) {
        DBRestaurant object = realm.createObject(DBRestaurant.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());
        object.setDisplayName(model.getDisplayName());

        object.setLatitude(model.getLatitude());
        object.setLongitude(model.getLongitude());
        object.setGoogleMapAddress(model.getGoogleMapAddress());
    }

    public void write(DBReview model, Realm realm) {
        DBReview object = realm.createObject(DBReview.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());

        object.setContent(model.getContent());
        object.setRate(model.getRate());
        object.setUserRef(model.getUserRef());
        object.setReviewRef(model.getReviewRef());
        object.setReviewType(model.getReviewType());
    }

    public void write(DBTeam model, Realm realm) {
        DBTeam object = realm.createObject(DBTeam.class); // Create a new object

        object.setUUID(model.getUUID());
        object.setObjectCreatedDate(model.getObjectCreatedDate());
        object.setDisplayName(model.getDisplayName());

        object.setEmail(model.getEmail());
        object.setAddress(model.getAddress());
    }

}
