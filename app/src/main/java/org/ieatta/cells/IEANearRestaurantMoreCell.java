package org.ieatta.cells;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.IEANearRestaurantMore;

public class IEANearRestaurantMoreCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEANearRestaurantMoreCell.class, R.layout.cell_near_restaurant_more);
    }

    private IEANearRestaurantMoreCell self = this;
    private ImageView iconImageView;
    private TextView titleLabel;

    public IEANearRestaurantMoreCell(View itemView) {
        super(itemView);

        self.iconImageView = (ImageView) itemView.findViewById(R.id.avatarView);
        self.titleLabel = (TextView) itemView.findViewById(R.id.moreCellTextView);
    }

    @Override
    public void render(Object value) {
        IEANearRestaurantMore more = (IEANearRestaurantMore) value;

        self.iconImageView.setImageResource(more.iconResId);
        self.titleLabel.setText(more.titleResId);
    }
}
