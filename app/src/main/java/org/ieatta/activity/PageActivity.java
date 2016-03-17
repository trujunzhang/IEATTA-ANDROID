package org.ieatta.activity;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import com.tableview.manage.RecycleViewManager;

import org.ieatta.R;
import org.ieatta.activity.fragments.NearRestaurantsFragment;
import org.wikipedia.activity.PlatformSingleFragmentActivity;

public class PageActivity extends PlatformSingleFragmentActivity<NearRestaurantsFragment> {
    private RecycleViewManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_near_restaurants);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        manager = new RecycleViewManager(this.getApplicationContext());
//        this.setupUI();
//        this.manager.setSectionItems(IEANearRestaurantMore.getMoresItems(), NearRestaurantSection.sectionMoreItems.ordinal());
    }

//    private void setupUI() {
//        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantsCell.getType(), NearRestaurantSection.sectionRestaurants.ordinal());
//        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantMoreCell.getType(), NearRestaurantSection.sectionMoreItems.ordinal());
//
//        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.More), NearRestaurantSection.sectionMoreItems.ordinal());
//    }

    private void configModelsInMoreSection() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected NearRestaurantsFragment createFragment(){
        return NearRestaurantsFragment.newInstance();
    }

    @Override
    protected void addFragment(NearRestaurantsFragment fragment) {

    }
}
