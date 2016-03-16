package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.database.models.DBRestaurant;

public class IEANearRestaurantsCell extends IEAViewHolder {

    public static CellType getType() {
        return new CellType(IEANearRestaurantsCell.class, R.layout.near_restaurant_cell);
    }

    private IEANearRestaurantsCell self = this;

//    private AvatarView avatarView;

    private TextView titleLabel;
    private TextView subtitleLabel;

    public IEANearRestaurantsCell(View itemView) {
        super(itemView);
//        self.avatarView = (AvatarView) itemView.findViewById(R.id.avatarView);
        self.titleLabel = (TextView) itemView.findViewById(R.id.titleTextView);
        self.subtitleLabel = (TextView) itemView.findViewById(R.id.addressTextView);
    }

    @Override
    public void render(Object value) {
        DBRestaurant model = (DBRestaurant) value;
        self.titleLabel.setText(model.getDisplayName());
        self.subtitleLabel.setText(((DBRestaurant) value).getGoogleMapAddress());

//        self.avatarView.loadNewPhotoByModel(model, R.drawable.blank_biz);
    }
}
