package org.ieatta.tasks;

import org.ieatta.activity.LeadImage;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.gallery.GalleryItem;
import org.ieatta.analytics.DBConvertFunnel;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import io.realm.RealmResults;

public class DBConvert {

    public static List<GalleryItem> toGalleryItem(RealmResults<DBPhoto> photos){
        List<GalleryItem> list =new LinkedList<>();
        for(DBPhoto photo : photos){
            File file = ThumbnailImageUtil.sharedInstance.getCacheImageUrl(photo);
            GalleryItem item = new GalleryItem(photo.getUUID(),"file://"+file.getAbsolutePath());
            new DBConvertFunnel().logToGalleryItem("toGalleryItem", "photo's path: " + file.getAbsolutePath());
            list.add(item);
        }
        return list;
    }

    public static LeadImageCollection toLeadImageCollection(RealmResults<DBPhoto> photos) {
        List<LeadImage> leadImages = new LinkedList<>();
        for(DBPhoto photo : photos){
            File localFile = ThumbnailImageUtil.sharedInstance.getCacheImageUrl(photo);
            LeadImage item = new LeadImage("file://"+localFile.getAbsolutePath(),photo.getOriginalUrl());
            new DBConvertFunnel().logToLeadImageCollection( item.getLocalUrl(),  item.getOnlineUrl());
            leadImages.add(item);
        }
        return new LeadImageCollection(leadImages);
    }
}
