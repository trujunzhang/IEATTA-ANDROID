package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import bolts.Continuation;
import bolts.Task;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.views.AvatarView;
import org.wikipedia.views.GoneIfEmptyTextView;


public class IEAOrderedPeopleCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAOrderedPeopleCell.class, R.layout.ordered_people_cell);
    }

    private AvatarView avatarView;

    private TextView nameLabel;
    private TextView addressLabel;
    private TextView addFoodButton;

    public IEAOrderedPeopleCell(View itemView) {
        super(itemView);
        this.avatarView = (AvatarView) itemView.findViewById(R.id.avatarView);
        this.nameLabel = (TextView) itemView.findViewById(R.id.titleTextView);
        this.addressLabel = (TextView) itemView.findViewById(R.id.addressTextView);
        this.addFoodButton = (TextView) itemView.findViewById(R.id.addFoodButton);
    }

    @Override
    public void render(Object value) {
        final IEAOrderedPeople model = (IEAOrderedPeople) value;

//        this.nameLabel.setText(model.model.displayName);
//        this.addressLabel.setText(model.model.address);
//        this.avatarView.loadNewPhotoByModel(model.model, R.drawable.blank_user_small);
//
//        this.addFoodButton.setVisibility(View.GONE);
//        if (model.hideButton == false) {
//            this.addFoodButton.setVisibility(View.VISIBLE);
//            this.addFoodButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    model.viewController.performSegueForAddingRecipe();
//                }
//            });
//        }
//
//        Recipe.queryOrderedRecipesCount(model.model, model.event)
//                .onSuccess(new Continuation<Integer, Object>() {
//                    @Override
//                    public Object then(Task<Integer> task) throws Exception {
//
//                        int recipesCount = task.getResult();
//                        model.model.recipesCount = recipesCount;
//                        String info = EnvironmentUtils.sharedInstance.getGlobalContext().getResources().getString(R.string.Recipes_Ordered_Count);
//                        info = recipesCount + " " + info;
//                        this.addressLabel.setText(info);
//
//                        return null;
//                    }
//                }, Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Object, Object>() {
//            @Override
//            public Object then(Task<Object> task) throws Exception {
//                if (task.isFaulted()) {
//
//                    String info = EnvironmentUtils.sharedInstance.getGlobalContext().getResources().getString(R.string.Fetch_recipe_count_failure);
//                    this.addressLabel.setText(info);
//
//                }
//                return null;
//            }
//        }, Task.UI_THREAD_EXECUTOR);
    }

}
