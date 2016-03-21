package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.views.AvatarView;
import org.wikipedia.views.GoneIfEmptyTextView;

public class IEANearRestaurantsCell extends IEAViewHolder {

    public static CellType getType() {
        return new CellType(IEANearRestaurantsCell.class, R.layout.cell_near_restaurant);
    }
    private AvatarView avatarView;
    private TextView titleLabel;
    private GoneIfEmptyTextView subtitleLabel;

    public IEANearRestaurantsCell(View itemView) {
        super(itemView);
        this.avatarView = (AvatarView) itemView.findViewById(R.id.page_list_item_image);
        this.titleLabel = (TextView) itemView.findViewById(R.id.page_list_item_title);
        this.subtitleLabel = (GoneIfEmptyTextView) itemView.findViewById(R.id.page_list_item_description);
    }

    @Override
    public void render(Object value) {
        DBRestaurant model = (DBRestaurant) value;

        this.titleLabel.setText(model.getDisplayName());
        this.subtitleLabel.setText(((DBRestaurant) value).getGoogleMapAddress());

        this.avatarView.loadNewPhotoByModel(model.getUUID());
    }
}
