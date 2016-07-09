package org.ieatta.tasks;

import org.ieatta.activity.LeadImage;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.gallery.GalleryItem;
import org.ieatta.analytics.DBConvertFunnel;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.cells.model.RecipeModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.parse.AppConstant;
import org.ieatta.server.cache.BaseImageUtil;
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
            new DBConvertFunnel().logToGalleryItem("toGalleryItem", "photo's path: " + file.getAbsolutePath());
            list.add(new GalleryItem(photo.getUUID(), BaseImageUtil.getLocalAbstractPath(file), photo.getOriginalUrl()));
        }
        return list;
    }

    public static LeadImageCollection toLeadImageCollection(RealmResults<DBPhoto> photos) {
        List<LeadImage> leadImages = new LinkedList<>();
        for (DBPhoto photo : photos) {
            File file = ThumbnailImageUtil.sharedInstance.getCacheImageUrl(photo);
            LeadImage item = new LeadImage(BaseImageUtil.getLocalAbstractPath(file), photo.getOriginalUrl(), photo.getUUID());
            new DBConvertFunnel().logToLeadImageCollection(item.getLocalUrl(), item.getOnlineUrl());
            leadImages.add(item);
        }
        return new LeadImageCollection(leadImages);
    }


    public static GalleryCollection toGalleryCollection(List<File> files) {
        List<GalleryItem> galleryItems = new LinkedList<>();
        for (File photoFile : files) {
            String uuid = photoFile.getName().split("_")[1];
            GalleryItem item = new GalleryItem(uuid, BaseImageUtil.getLocalAbstractPath(photoFile));
            galleryItems.add(item);
        }
        return new GalleryCollection(galleryItems);
    }

    private static DBTeam getTeam(String userRef, RealmResults<DBTeam> teams) {
        for (DBTeam team : teams) {
            if (team.getUUID().equals(userRef))
                return team;
        }
        return AppConstant.getAnonymousUser();
    }

    public static List<IEAReviewsCellModel> toReviewsCellModels(RealmResults<DBReview> reviews, RealmResults<DBTeam> teams) {
        List<IEAReviewsCellModel> list = new LinkedList<>();

        for (int i = 0; i < teams.size(); i++) {
            DBReview review = reviews.get(i);
            list.add(new IEAReviewsCellModel(review, DBConvert.getTeam(review.getUserRef(), teams)));
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


    public static List<String> getPeoplePoints(List<DBPeopleInEvent> peopleInEvent) {
        List<String> peoplePoints = new LinkedList<>();

        for (DBPeopleInEvent model : peopleInEvent) {
            peoplePoints.add(model.getUserRef());
        }

        return peoplePoints;
    }

    public static List<RecipeModel> toRecipeModels(RealmResults<DBRecipe> recipes, OrderedRecipesTask task) {
        List<RecipeModel> recipeModels = new LinkedList<>();
        for (DBRecipe model : recipes) {
            recipeModels.add(new RecipeModel(model, task));
        }
        return recipeModels;
    }
}
