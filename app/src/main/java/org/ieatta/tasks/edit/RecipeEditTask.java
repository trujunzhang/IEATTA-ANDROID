package org.ieatta.tasks.edit;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;

import org.ieatta.R;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.update.UpdateEntry;
import org.ieatta.cells.edit.IEAEditTextFieldCell;
import org.ieatta.cells.edit.IEAEditTextRecipeFieldCell;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
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

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class RecipeEditTask extends FragmentTask {

    @VisibleForTesting
    public RecipeEditTask(HistoryEntry entry) {
        super(entry);
    }

    public RecipeEditTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    enum EditRecipeSection {
        sectionInformation,//= 0
        section_gallery_thumbnail,//= 1
    }

    public DBRecipe recipe;

    /**
     * Execute Task for Restaurant edit.
     *
     * @return
     */
    @Override
    public Task<Void> executeTask() {
        final String recipeUUID = this.entry.getHPara();
        if (this.entry.isNewModel() == true)
            return Task.forResult(null);

        return new RealmModelReader<DBRecipe>(DBRecipe.class).getFirstObject(LocalDatabaseQuery.get(recipeUUID), false, realmList).onSuccessTask(new Continuation<DBRecipe, Task<List<File>>>() {
            @Override
            public Task<List<File>> then(Task<DBRecipe> task) throws Exception {
                recipe = task.getResult();
                return ThumbnailImageUtil.sharedInstance.getImagesListTask(recipeUUID);
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
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Recipe_Information), EditRecipeSection.sectionInformation.ordinal());
        this.manager.setRegisterCellClass(IEAEditTextFieldCell.getType(), EditRecipeSection.sectionInformation.ordinal());
        // **** Important (just for android)****
        this.manager.setRegisterCellClassInSpecialRow(IEAEditTextRecipeFieldCell.getType(), EditRecipeSection.sectionInformation.ordinal(), 1);
    }

    @Override
    public void postUI() {

    }

    @Override
    public boolean haveLeadImage() {
        return false;
    }
}
