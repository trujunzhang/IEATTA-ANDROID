package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.database.query.RecipeQuery;
import org.ieatta.views.AvatarView;
import org.wikipedia.views.GoneIfEmptyTextView;

import bolts.Continuation;
import bolts.Task;

public class IEAOrderedPeopleCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAOrderedPeopleCell.class, R.layout.cell_ordered_people);
    }

    private AvatarView avatarView;

    private TextView nameLabel;
    private GoneIfEmptyTextView recipesCountLabel;

    public IEAOrderedPeopleCell(View itemView) {
        super(itemView);
        this.avatarView = (AvatarView) itemView.findViewById(R.id.page_list_item_image);
        this.nameLabel = (TextView) itemView.findViewById(R.id.page_list_item_title);
        this.recipesCountLabel = (GoneIfEmptyTextView) itemView.findViewById(R.id.page_list_item_description);
    }

    @Override
    public void render(Object value) {
        final IEAOrderedPeople model = (IEAOrderedPeople) value;

        this.nameLabel.setText(model.getDisplayName());
        this.avatarView.loadNewPhotoByModel(model.getTeamUUID());

        new RecipeQuery().queryOrderedRecipesCount(model.getTeamUUID(),model.getEventUUID()).onSuccess(new Continuation<Long, Void>() {
            @Override
            public Void then(Task<Long> task) throws Exception {
                final String info = String.format("%d %s", task.getResult(), IEAApp.getInstance().getResources().getString(R.string.Recipes_Ordered_Count));
                IEAOrderedPeopleCell.this.recipesCountLabel.setText(info);
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);

    }
}
