package org.ieatta.tasks;

import org.ieatta.activity.gallery.GalleryItem;
import org.ieatta.database.models.DBPhoto;

import java.util.LinkedList;
import java.util.List;

import io.realm.RealmResults;

public class DBConvert {

    public static List<GalleryItem> toGalleryItem(RealmResults<DBPhoto> photos){
        List<GalleryItem> list =new LinkedList<>();

        return list;
    }

}
