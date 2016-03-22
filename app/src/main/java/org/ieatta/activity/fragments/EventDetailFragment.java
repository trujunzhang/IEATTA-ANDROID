package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.view.View;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.utils.CollectionUtil;

import org.ieatta.R;
import org.ieatta.cells.IEAGalleryThumbnailCell;
import org.ieatta.cells.IEAOrderedPeopleCell;
import org.ieatta.cells.IEARestaurantEventsCell;
import org.ieatta.cells.IEAReviewsCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAGalleryThumbnail;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.EventDetailTask;
import org.ieatta.tasks.RestaurantDetailTask;

import bolts.Continuation;
import bolts.Task;

public class EventDetailFragment extends DetailFragment {

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

    @Override
    public void onContentHeightChanged(int contentHeight) {
        this.manager.updateHeaderItem(new IEAHeaderViewModel(contentHeight));
    }
    enum EventDetailSection {
        section_ordered_people, //= 0
        section_reviews,       //= 1
    }

    private RecycleViewManager manager;

    private EventDetailTask task = new EventDetailTask();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }

    private void setupUI() {
        this.manager.setRegisterHeaderView(IEAHeaderView.getType());
        this.manager.setRegisterFooterView(IEAFooterView.getType());

        this.manager.setRegisterCellClass(IEAOrderedPeopleCell.getType(), EventDetailSection.section_ordered_people.ordinal());
        this.manager.setRegisterCellClass(IEAReviewsCell.getType(), EventDetailSection.section_reviews.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.People_Ordered), EventDetailSection.section_ordered_people.ordinal());
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Reviews), EventDetailSection.section_reviews.ordinal());
    }

    @Override
    public void loadPage() {
        manager.startManagingWithDelegate(webView);
        manager.setOnItemClickListener(itemClickListener);

        this.setupUI();

         String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
//        String restaurantUUID = "33ED9F31-F6A5-43A4-8D11-8E511CA0BD39"; // The Spice Jar
        String eventUUID = "07B2D33C-F11D-404B-9D78-016D16BEE9FE";
//        String eventUUID = "";
        task.executeTask(restaurantUUID,eventUUID).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                EventDetailFragment.this.reloadPage();
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                return null;
            }
        });
    }

    @Override
    protected void reloadPage() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.getScreenHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

//        this.manager.setSectionItems(task.events, RestaurantDetailSection.section_events.ordinal());

//        this.manager.setSectionItems(CollectionUtil.createList(new IEAGalleryThumbnail(this.task.thumbnailGalleryCollection, this.galleryViewListener)), RestaurantDetailSection.section_gallery_thumbnail.ordinal());
//        this.manager.setSectionItems(task.reviewsCellModelList, RestaurantDetailSection.section_reviews.ordinal());

//        model.setPage(task.getPage());

        super.reloadPage();
    }

}
