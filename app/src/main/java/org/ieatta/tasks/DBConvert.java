package org.ieatta.tasks;

import org.ieatta.activity.LeadImage;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.gallery.GalleryItem;
import org.ieatta.analytics.DBConvertFunnel;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.cells.model.ReviewsCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.parse.DBConstant;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import io.realm.RealmResults;

public class DBConvert {

    public static List<GalleryItem> toGalleryItem(RealmResults<DBPhoto> photos) {
        List<GalleryItem> list = new LinkedList<>();
        for (DBPhoto photo : photos) {
            File file = ThumbnailImageUtil.sharedInstance.getCacheImageUrl(photo);
            GalleryItem item = new GalleryItem(photo.getUUID(), "file://" + file.getAbsolutePath());
            new DBConvertFunnel().logToGalleryItem("toGalleryItem", "photo's path: " + file.getAbsolutePath());
            list.add(item);
        }
        return list;
    }

    public static LeadImageCollection toLeadImageCollection(RealmResults<DBPhoto> photos) {
        List<LeadImage> leadImages = new LinkedList<>();
        for (DBPhoto photo : photos) {
            File localFile = ThumbnailImageUtil.sharedInstance.getCacheImageUrl(photo);
            LeadImage item = new LeadImage("file://" + localFile.getAbsolutePath(), photo.getOriginalUrl());
            new DBConvertFunnel().logToLeadImageCollection(item.getLocalUrl(), item.getOnlineUrl());
            leadImages.add(item);
        }
        return new LeadImageCollection(leadImages);
    }


    public static GalleryCollection toGalleryCollection(List<File> files) {
        List<GalleryItem> galleryItems = new LinkedList<>();
        for (File photoFile : files) {
            String uuid = photoFile.getName().split("_")[1];
            String thumbUrl = "file://" + photoFile.getAbsolutePath();
            GalleryItem item = new GalleryItem(uuid, thumbUrl);
            galleryItems.add(item);
        }
        return new GalleryCollection(galleryItems);
    }

    private static DBTeam getTeamUUID(String userRef, RealmResults<DBTeam> teams) {
        for (DBTeam team : teams) {
            if (team.getUUID().equals(userRef))
                return team;
        }
        return DBConstant.getAnonymousUser();
    }

    public static List<ReviewsCellModel> toReviewsCellModels(RealmResults<DBReview> reviews, RealmResults<DBTeam> teams) {
        List<ReviewsCellModel> list = new LinkedList<>();
        for (DBReview review : reviews) {
            ReviewsCellModel item = new ReviewsCellModel(review, DBConvert.getTeamUUID(review.getUserRef(), teams));
            list.add(item);
        }
        return list;
    }

    public static List<IEAOrderedPeople> toOrderedPeopleList(RealmResults<DBTeam> teams, DBEvent event) {
        List<IEAOrderedPeople> list = new LinkedList<>();
        for (DBTeam team : teams) {
            list.add(new IEAOrderedPeople(team.getUUID(), team.getDisplayName(), event.getUUID()));
        }
        return list;
    }

}
