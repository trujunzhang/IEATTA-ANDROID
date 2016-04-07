package org.ieatta.tasks.edit;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;

import org.ieatta.R;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.update.UpdateEntry;
import org.ieatta.cells.edit.IEADatePickerCell;
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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class EventEditTask extends FragmentTask {

    @VisibleForTesting
    public EventEditTask(HistoryEntry entry) {
        super(entry);
    }

    public EventEditTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    enum EditEventSection {
        sectionInformation,//= 0
        section_gallery_thumbnail,//= 1
        sectionDurationDate,//= 2
    }

    public DBEvent event = new DBEvent();

    /**
     * Execute Task for Event edit.
     *
     * @return
     */
    @Override
    public Task<Void> executeTask() {
        final String eventUUID = this.entry.getHPara();
        if (this.entry.isNewModel() == true)
            return Task.forResult(null);

        new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(eventUUID), false, this.realmList).onSuccessTask(new Continuation<DBEvent, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBEvent> task) throws Exception {
                event = task.getResult();
                return LocalDatabaseQuery.queryPhotosByModel(eventUUID, PhotoUsedType.PU_Waiter.getType(), realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                thumbnailGalleryCollection = new GalleryCollection(DBConvert.toGalleryItem(task.getResult()));
                return null;
            }
        });

        return null;
    }

    @Override
    public void prepareUI() {
        super.prepareUI();

        // Add rows for sections.
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Event_Information), EditEventSection.sectionInformation.ordinal());
        this.manager.setRegisterCellClass(IEAEditTextFieldCell.getType(), EditEventSection.sectionInformation.ordinal());
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.model.getActionbarHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        List<EditCellModel> editCellModelList = new LinkedList<EditCellModel>() {{
            add(new EditCellModel(IEAEditKey.event_name, event.getDisplayName(), R.string.Event_Name_info));
        }};
        this.manager.setSectionItems(editCellModelList, EditEventSection.sectionInformation.ordinal());

        postPhotosGallery(EditEventSection.section_gallery_thumbnail.ordinal());

        model.setPage(new Page());
    }


    @Override
    public boolean haveLeadImage() {
        return false;
    }
}
