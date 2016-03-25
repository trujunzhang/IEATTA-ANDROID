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
    private ObservableWebView webView;

    private RestaurantDetailTask task ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initWithRecycleView();

        initWithRatioLayout();
    }

    private void initWithRatioLayout() {
        setContentView(R.layout.activity_ratio);

    }

    private void initWithRecycleView() {
        setContentView(R.layout.activity_effect);

        this.webView = (ObservableWebView) findViewById(R.id.recycleView);
    }

}
