package org.ieatta.tasks;


import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.query.LocalDatabaseQuery;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class RestaurantDetailTask {
    private DBRestaurant restaurant;
    private List<DBEvent> events;
    private List<DBReview> reviews;
    private List<DBPhoto> galleryCollection;

    /**
     * Execute Task for Restaurant detail.
     * @param UUID    restaurant's UUID
     * @return
     */
    public Task<Void> executeTask(final String UUID){
        new LocalDatabaseQuery(DBRestaurant.class).fetchObject(UUID).onSuccessTask(new Continuation<DBRestaurant, Task<List<DBPhoto>>>() {
            @Override
            public Task<List<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                RestaurantDetailTask.this.restaurant = task.getResult();
                return LocalDatabaseQuery.queryPhotosForRestaurant(UUID);
            }
        }).onSuccessTask(new Continuation<List<DBPhoto>,Task<List<DBEvent>>>() {
            @Override
            public Task<List<DBEvent>> then(Task<List<DBPhoto>> task) throws Exception {
                return null;
            }
        });



//        Event.queryEventsRelatedRestaurant(self.restaurant)
//                .onSuccessTask(new Continuation<List<ParseModelAbstract>, Task<List<ParseModelAbstract>>>() {
//                    @Override
//                    public Task<List<ParseModelAbstract>> then(Task<List<ParseModelAbstract>> task) throws Exception {
//                        self.fetchedEvents = task.getResult();
//
//                        return Photo.queryPhotosByRestaurant(self.restaurant);
//                    }
//                }).onSuccessTask(new Continuation<List<ParseModelAbstract>, Task<Boolean>>() {
//            @Override
//            public Task<Boolean> then(Task<List<ParseModelAbstract>> task) throws Exception {
//                self.fetchedPhotosTask = task;
//
//                // Next, Load Reviews.
//                return self.getReviewsRelatedModelQueryTask();
//            }
//        }).onSuccess(new Continuation<Boolean, Object>() {
//            @Override
//            public Object then(Task<Boolean> task) throws Exception {
//                self.setRegisterCellClass(IEARestaurantDetailHeaderCell.getType(), RestaurantDetailSection.sectionHeader.ordinal());
//                self.setSectionItems(CollectionUtils.createList(new IEARestaurantDetailHeader(self, self.restaurant)), RestaurantDetailSection.sectionHeader.ordinal());
//
//                self.showGoogleMapAddress(RestaurantDetailSection.sectionGoogleMapAddress.ordinal());
//                self.setRegisterCellClassWhenSelected(IEARestaurantEventsCell.getType(), RestaurantDetailSection.sectionEvents.ordinal());
//                self.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.sectionEvents.ordinal());
//
//                self.configureDetailSection(self.fetchedEvents, R.string.Empty_for_Event, IEARestaurantEventsCell.getType(),RestaurantDetailSection.sectionEvents.ordinal());
//                self.configureReviewsSection();
//                self.configurePhotoGallerySection(fetchedPhotosTask);
//
//                return null;
//            }
//        }, Task.UI_THREAD_EXECUTOR);

        return null;
    }

}
