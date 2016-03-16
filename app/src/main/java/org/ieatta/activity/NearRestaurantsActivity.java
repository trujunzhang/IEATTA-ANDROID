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
    private RecycleViewManager manager;

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

        manager = new RecycleViewManager(this.getApplicationContext());

        this.setupUI();
    }

    private void setupUI() {
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantsCell.getType(), NearRestaurantSection.sectionRestaurants.ordinal());
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantMoreCell.getType(), NearRestaurantSection.sectionMoreItems.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.More), NearRestaurantSection.sectionMoreItems.ordinal());
    }

    private void configModelsInMoreSection() {

        this.manager.setSectionItems(new List(getMoresItems()), NearRestaurantSection.sectionMoreItems.ordinal());
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
