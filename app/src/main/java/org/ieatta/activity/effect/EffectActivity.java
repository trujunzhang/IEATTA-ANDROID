package org.ieatta.activity.effect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tableview.RecycleViewManager;

import org.ieatta.R;
import org.ieatta.tasks.RestaurantDetailTask;
import org.ieatta.views.ObservableWebView;
import android.os.Bundle;
import android.view.View;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;

import org.ieatta.R;
import org.ieatta.cells.IEAGalleryThumbnailCell;
import org.ieatta.cells.IEARestaurantEventsCell;
import org.ieatta.cells.IEAReviewsCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.RestaurantDetailTask;

import bolts.Continuation;
import bolts.Task;

import static butterknife.ButterKnife.findById;

public class EffectActivity extends AppCompatActivity {


    enum RestaurantDetailSection {
        section_events,//= 0
        section_gallery_thumbnail,//= 1
        section_reviews,//= 2
    }

    private ObservableWebView webView;

    private RecycleViewManager manager;
    private RestaurantDetailTask task = new RestaurantDetailTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        this.webView = (ObservableWebView) findViewById(R.id.recycleView);

        manager = new RecycleViewManager(this.getApplicationContext());

        this.setupUI();
    }

    private void setupUI() {
        this.manager.setRegisterHeaderView(IEAHeaderView.getType());
        this.manager.setRegisterFooterView(IEAFooterView.getType());

        this.manager.setRegisterCellClass(IEARestaurantEventsCell.getType(), RestaurantDetailSection.section_events.ordinal());
        this.manager.setRegisterCellClass(IEAGalleryThumbnailCell.getType(), RestaurantDetailSection.section_gallery_thumbnail.ordinal());
        this.manager.setRegisterCellClass(IEAReviewsCell.getType(), RestaurantDetailSection.section_reviews.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.section_events.ordinal());
//        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Reviews), RestaurantDetailSection.section_reviews.ordinal());
    }

    public void loadPage() {
        manager.startManagingWithDelegate(webView);

        this.setupUI();

        String uuid = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
//        String uuid = "33ED9F31-F6A5-43A4-8D11-8E511CA0BD39"; // The Spice Jar
        task.executeTask(uuid).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                EffectActivity.this.reloadPage();
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                return null;
            }
        });
    }


    protected void reloadPage() {
//        this.manager.setHeaderItem(new IEAHeaderViewModel(this.getScreenHeight()), IEAHeaderView.getType());
        this.manager.setHeaderItem(new IEAHeaderViewModel(800), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

//        this.manager.setSectionItems(task.events, RestaurantDetailSection.section_events.ordinal());

//        this.manager.setSectionItems(CollectionUtil.createList(new IEAGalleryThumbnail(this.task.thumbnailGalleryCollection,this.galleryViewListener)), RestaurantDetailSection.section_gallery_thumbnail.ordinal());
//        this.manager.setSectionItems(task.reviewsCellModelList, RestaurantDetailSection.section_reviews.ordinal());

//        super.reloadPage();
    }

}
