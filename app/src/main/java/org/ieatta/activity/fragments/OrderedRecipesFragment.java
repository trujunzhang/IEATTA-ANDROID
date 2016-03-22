package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.view.View;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;

import org.ieatta.R;
import org.ieatta.cells.IEAOrderedRecipeCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.OrderedRecipesTask;

import bolts.Continuation;
import bolts.Task;

public class OrderedRecipesFragment extends DetailFragment {

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

    @Override
    public void onContentHeightChanged(int contentHeight) {
        this.manager.updateHeaderItem(new IEAHeaderViewModel(contentHeight));
    }

    enum OrderedRecipesSection {
        section_recipes,       //= 0
    }

    private RecycleViewManager manager;

    private OrderedRecipesTask task = new OrderedRecipesTask();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }

    private void setupUI() {
        this.manager.setRegisterHeaderView(IEAHeaderView.getType());
        this.manager.setRegisterFooterView(IEAFooterView.getType());

        this.manager.setRegisterCellClass(IEAOrderedRecipeCell.getType(), OrderedRecipesSection.section_recipes.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Ordered_Recipes), OrderedRecipesSection.section_recipes.ordinal());
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
        String teamUUID = "197C0BEF-B432-47B8-988B-99406643623A";// Dolores Chavez
        task.executeTask(restaurantUUID, eventUUID, teamUUID).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                OrderedRecipesFragment.this.reloadPage();
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

//        this.manager.setSectionItems(task.orderedPeopleList, EventDetailSection.section_ordered_people.ordinal());

//        model.setPage(task.getPage());

        super.reloadPage();
    }

}
