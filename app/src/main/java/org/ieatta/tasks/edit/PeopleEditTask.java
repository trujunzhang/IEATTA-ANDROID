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
import org.ieatta.tasks.DBConvert;
import org.ieatta.tasks.FragmentTask;

import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class PeopleEditTask extends FragmentTask {

    @VisibleForTesting
    public PeopleEditTask(HistoryEntry entry) {
        super(entry);
    }

    public PeopleEditTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    @Override
    public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

    }

    enum EditPeopleSection {
        sectionInformation, // =  0
        sectionPhotos, // =  1
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
        final String eventUUID = this.entry.getHPara();

        return new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(eventUUID), false, this.realmList).onSuccessTask(new Continuation<DBEvent, Task<DBRestaurant>>() {
            @Override
            public Task<DBRestaurant> then(Task<DBEvent> task) throws Exception {
                DBEvent event = task.getResult();
                PeopleEditTask.this.event = event;
                PeopleEditTask.this.restaurantUUID = event.getRestaurantRef();
                return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                DBRestaurant restaurant = task.getResult();
                PeopleEditTask.this.restaurant = restaurant;
                return LocalDatabaseQuery.queryPhotosByModel(restaurantUUID, PhotoUsedType.PU_Restaurant.getType(), realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBPeopleInEvent>>>() {
            @Override
            public Task<RealmResults<DBPeopleInEvent>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                PeopleEditTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                return new RealmModelReader<DBPeopleInEvent>(DBPeopleInEvent.class).fetchResults(
                        LocalDatabaseQuery.getQueryOrderedPeople(eventUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPeopleInEvent>, Task<RealmResults<DBTeam>>>() {
            @Override
            public Task<RealmResults<DBTeam>> then(Task<RealmResults<DBPeopleInEvent>> task) throws Exception {
                List<String> peoplePoints = DBConvert.getPeoplePoints(task.getResult());
                return new RealmModelReader<DBTeam>(DBTeam.class).fetchResults(LocalDatabaseQuery.getObjectsByUUIDs(peoplePoints), false, realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBTeam>, Task<List<IEAReviewsCellModel>>>() {
            @Override
            public Task<List<IEAReviewsCellModel>> then(Task<RealmResults<DBTeam>> task) throws Exception {
                PeopleEditTask.this.orderedPeopleList = DBConvert.toOrderedPeopleList(task.getResult(), PeopleEditTask.this.event);
                return new ReviewQuery().queryReview(eventUUID, ReviewType.Review_Event, AppConstant.limit_reviews);
            }
        }).onSuccess(new Continuation<List<IEAReviewsCellModel>, Void>() {
            @Override
            public Void then(Task<List<IEAReviewsCellModel>> task) throws Exception {
                PeopleEditTask.this.reviewsCellModelList = task.getResult();
                return null;
            }
        });
    }

    @Override
    public Task<Void> executeUpdateTask(UpdateEntry entry) {
        return null;
    }


    @Override
    public void prepareUI() {
        super.prepareUI();

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.People_Information), EditPeopleSection.sectionInformation.ordinal());
        this.manager.setRegisterCellClass(IEAEditTextFieldCell.getType(), EditPeopleSection.sectionInformation.ordinal());
    }

    @Override
    public void postUI() {

    }
}
