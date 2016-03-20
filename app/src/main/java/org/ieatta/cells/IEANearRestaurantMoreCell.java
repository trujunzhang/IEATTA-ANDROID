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

    private ImageView iconImageView;
    private TextView titleLabel;

    public IEANearRestaurantMoreCell(View itemView) {
        super(itemView);

        this.iconImageView = (ImageView) itemView.findViewById(R.id.avatarView);
        this.titleLabel = (TextView) itemView.findViewById(R.id.moreCellTextView);
    }

    @Override
    public void render(Object value) {
        IEANearRestaurantMore more = (IEANearRestaurantMore) value;

        this.iconImageView.setImageResource(more.iconResId);
        this.titleLabel.setText(more.titleResId);
    }
}
