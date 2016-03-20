package org.ieatta.tasks;

import org.ieatta.activity.gallery.GalleryItem;
import org.ieatta.analytics.RecycleCellFunnel;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import io.realm.RealmResults;

public class DBConvert {

    public static List<GalleryItem> toGalleryItem(RealmResults<DBPhoto> photos){
        List<GalleryItem> list =new LinkedList<>();
        for(DBPhoto photo : photos){
            File file = ThumbnailImageUtil.sharedInstance.getCacheImageUrl(photo);
            GalleryItem item = new GalleryItem("","file://"+file.getAbsolutePath());
            new RecycleCellFunnel().logCellInfo("toGalleryItem","photo's path: "+file.getAbsolutePath());
            list.add(item);
        }
        return list;
    }

}
