package org.ieatta.tasks.edit;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;
import com.tableview.utils.CollectionUtil;

import org.ieatta.R;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.edit.IEAEditTextFieldCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.EditCellModel;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.query.ReviewQuery;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.AppConstant;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.tasks.DBConvert;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.tasks.RecipeDetailTask;

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

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

    public DBRestaurant restaurant;
    public DBEvent event;
    public List<IEAOrderedPeople> orderedPeopleList;
    private LeadImageCollection leadImageCollection; // for restaurants

    /**
     * Execute Task for Restaurant edit.
     *
     * @return
     */
    @Override
    public Task<Void> executeTask() {
        final String restaurantUUID = this.entry.getHPara();

        return ThumbnailImageUtil.sharedInstance.getImagesListTask(restaurantUUID).onSuccessTask(new Continuation<List<File>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<File>> task) throws Exception {
                RestaurantEditTask.this.thumbnailGalleryCollection = DBConvert.toGalleryCollection(task.getResult());
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

//        if (this.newModel == false) {
//            this.manager.showGoogleMapAddress(EditRestaurantSection.sectionGoogleMapAddress.ordinal());
//        }
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.model.getActionbarHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        EditCellModel cellModel = new EditCellModel(IEAEditKey.rest_name, "wh", R.string.Restaurant_Name_info);
        this.editCellModelList.add(cellModel);
        this.manager.setSectionItems(this.editCellModelList, EditRestaurantSection.sectionInformation.ordinal());

        postPhotosGallery(EditRestaurantSection.section_gallery_thumbnail.ordinal());
    }
}
