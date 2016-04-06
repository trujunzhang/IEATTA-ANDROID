package org.ieatta.tasks.edit;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;

import org.ieatta.R;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.update.UpdateEntry;
import org.ieatta.cells.edit.IEAEditTextFieldCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.EditCellModel;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.tasks.DBConvert;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.tasks.RestaurantDetailTask;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class RestaurantEditTask extends FragmentTask {

    @VisibleForTesting
    public RestaurantEditTask(HistoryEntry entry) {
        super(entry);
    }

    public RestaurantEditTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    enum EditRestaurantSection {
        sectionInformation,//= 0
        section_gallery_thumbnail,//= 1
        sectionGoogleMapAddress,//= 2
    }

    public DBRestaurant restaurant = new DBRestaurant();

    /**
     * Execute Task for Restaurant edit.
     *
     * @return
     */
    @Override
    public Task<Void> executeTask() {
        final String restaurantUUID = this.entry.getHPara();
        if (this.entry.isNewModel() == true)
            return Task.forResult(null);

        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false, realmList).onSuccessTask(new Continuation<DBRestaurant, Task<List<File>>>() {
            @Override
            public Task<List<File>> then(Task<DBRestaurant> task) throws Exception {
                restaurant = task.getResult();
                return ThumbnailImageUtil.sharedInstance.getImagesListTask(restaurantUUID);
            }
        }).onSuccessTask(new Continuation<List<File>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<File>> task) throws Exception {
                thumbnailGalleryCollection = DBConvert.toGalleryCollection(task.getResult());
                return null;
            }
        });
    }

    @Override
    public void prepareUI() {
        super.prepareUI();

        // Add rows for sections.
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Restaurant_Information), EditRestaurantSection.sectionInformation.ordinal());
        this.manager.setRegisterCellClass(IEAEditTextFieldCell.getType(), EditRestaurantSection.sectionInformation.ordinal());
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.model.getActionbarHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        List<EditCellModel> editCellModelList = new LinkedList<EditCellModel>() {{
            add(new EditCellModel(IEAEditKey.rest_name, restaurant.getDisplayName(), R.string.Restaurant_Name_info));
        }};
        this.manager.setSectionItems(editCellModelList, EditRestaurantSection.sectionInformation.ordinal());

        postPhotosGallery(EditRestaurantSection.section_gallery_thumbnail.ordinal());

        model.setPage(new Page());
    }

    @Override
    public boolean haveLeadImage() {
        return false;
    }
}
