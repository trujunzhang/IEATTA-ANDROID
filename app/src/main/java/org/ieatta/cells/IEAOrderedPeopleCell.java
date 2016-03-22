package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.IEAOrderedPeople;
import org.ieatta.views.AvatarView;


public class IEAOrderedPeopleCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAOrderedPeopleCell.class, R.layout.cell_ordered_people);
    }

    private AvatarView avatarView;

    private TextView nameLabel;
    private TextView recipesCountLabel;

    public IEAOrderedPeopleCell(View itemView) {
        super(itemView);
        this.avatarView = (AvatarView) itemView.findViewById(R.id.avatarView);
        this.nameLabel = (TextView) itemView.findViewById(R.id.titleTextView);
        this.recipesCountLabel = (TextView) itemView.findViewById(R.id.recipesCountTextView);
    }

    @Override
    public void render(Object value) {
        final IEAOrderedPeople model = (IEAOrderedPeople) value;

        this.nameLabel.setText(model.getDisplayName());
        this.avatarView.loadNewPhotoByModel(model.getTeamUUID());
    }
}
