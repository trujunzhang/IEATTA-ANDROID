package org.ieatta.tasks.edit;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;

import org.ieatta.R;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.IEAOrderedPeopleCell;
import org.ieatta.cells.IEAReviewsCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
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
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.DBConvert;
import org.ieatta.tasks.FragmentTask;
import org.wikipedia.util.DimenUtil;

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

    @Override
    public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

    }

    enum EventDetailSection {
        section_ordered_people, //= 0
        section_reviews,       //= 1
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
                RecipeEditTask.this.event = event;
                RecipeEditTask.this.restaurantUUID = event.getRestaurantRef();
                return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                DBRestaurant restaurant = task.getResult();
                RecipeEditTask.this.restaurant = restaurant;
                return LocalDatabaseQuery.queryPhotosByModel(restaurantUUID, PhotoUsedType.PU_Restaurant.getType(), realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBPeopleInEvent>>>() {
            @Override
            public Task<RealmResults<DBPeopleInEvent>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                RecipeEditTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
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
                RecipeEditTask.this.orderedPeopleList = DBConvert.toOrderedPeopleList(task.getResult(), RecipeEditTask.this.event);
                return new ReviewQuery().queryReview(eventUUID, ReviewType.Review_Event);
            }
        }).onSuccess(new Continuation<List<IEAReviewsCellModel>, Void>() {
            @Override
            public Void then(Task<List<IEAReviewsCellModel>> task) throws Exception {
                RecipeEditTask.this.reviewsCellModelList = task.getResult();
                return null;
            }
        });
    }


    public Page getPage() {
        String title = restaurant.getDisplayName();
        PageTitle pageTitle = new PageTitle(this.event.getUUID());
        PageProperties properties = new PageProperties(this.leadImageCollection, title);

        return new Page(pageTitle, properties);
    }

    @Override
    public void prepareUI() {
        super.prepareUI();

        this.manager.setRegisterCellClass(IEAOrderedPeopleCell.getType(), EventDetailSection.section_ordered_people.ordinal());
        this.manager.setRegisterCellClass(IEAReviewsCell.getType(), EventDetailSection.section_reviews.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.People_Ordered), EventDetailSection.section_ordered_people.ordinal());
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(DimenUtil.getDisplayWidthPx()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        this.manager.setSectionItems(this.orderedPeopleList, EventDetailSection.section_ordered_people.ordinal());

        postReviews(EventDetailSection.section_reviews.ordinal());

        model.setPage(this.getPage());
    }
}
