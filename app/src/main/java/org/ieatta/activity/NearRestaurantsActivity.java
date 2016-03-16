package org.ieatta.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tableview.manage.RecycleViewManager;
import com.tableview.storage.DTTableViewManager;
import com.tableview.utils.CollectionUtil;

import org.ieatta.R;
import org.ieatta.cells.IEANearRestaurantMoreCell;
import org.ieatta.cells.IEANearRestaurantsCell;
import org.ieatta.cells.model.IEANearRestaurantMore;
import org.ieatta.provide.MainSegueIdentifier;

import java.util.List;

enum NearRestaurantSection {
    sectionMoreItems,//= 0
    sectionRestaurants, //= 1
}

public class NearRestaurantsActivity extends AppCompatActivity {
    private RecycleViewManager manager = new RecycleViewManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_restaurants);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.setupUI();
    }

    private void setupUI() {
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantsCell.getType(), NearRestaurantSection.sectionRestaurants.ordinal());
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantMoreCell.getType(), NearRestaurantSection.sectionMoreItems.ordinal());
    }

    private void configModelsInMoreSection() {
//        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.More), NearRestaurantSection.sectionMoreItems.ordinal());
//        this.manager.setSectionItems(new List(getMoresItems()), NearRestaurantSection.sectionMoreItems.ordinal());
    }

    @NonNull
    private List<IEANearRestaurantMore> getMoresItems() {
        IEANearRestaurantMore managerRestaurantItem = new IEANearRestaurantMore(R.drawable.restaurants_icon, R.string.Add_a_Restaurant, MainSegueIdentifier.editRestaurantSegueIdentifier);
        IEANearRestaurantMore searchRestaurant = new IEANearRestaurantMore(R.drawable.nav_search, R.string.Search_Restaurants, MainSegueIdentifier.searchRestaurantSegueIdentifier);
        IEANearRestaurantMore managerPeople = new IEANearRestaurantMore(R.drawable.nav_add_friends, R.string.Manage_Friends, MainSegueIdentifier.managerPeopleSegueIdentifier);
        IEANearRestaurantMore readReviews = new IEANearRestaurantMore(R.drawable.nav_passport_reviews, R.string.Read_Reviews, MainSegueIdentifier.readReviewsSegueIdentifier);

        return CollectionUtil.createList(new IEANearRestaurantMore[]{managerRestaurantItem, searchRestaurant, managerPeople, readReviews});
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

}
