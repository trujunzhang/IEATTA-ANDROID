package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.utils.DBUtil;

public class IEARestaurantEventsCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEARestaurantEventsCell.class, R.layout.cell_restaurant_events);
    }

    private TextView infoLabel;
    private TextView timeInfoLabel;
    private TextView timeAgoLabelLabel;

    public IEARestaurantEventsCell(View itemView) {
        super(itemView);

        this.infoLabel = (TextView) itemView.findViewById(R.id.titleTextView);
        this.timeInfoLabel = (TextView) itemView.findViewById(R.id.infoTextView);
        this.timeAgoLabelLabel = (TextView) itemView.findViewById(R.id.timeAgoTextView);
    }

    @Override
    public void render(Object value) {
        DBEvent more = (DBEvent) value;

        this.infoLabel.setText(more.getDisplayName());
        String waiter = more.getWaiter();
        if (waiter == null || waiter.equals("")) {
            this.timeInfoLabel.setText(R.string.No_waiters_servered_for_you);
        } else {
            this.timeInfoLabel.setText(waiter);
        }

        this.timeAgoLabelLabel.setText(DBUtil.getTimeAgoString(more.getObjectCreatedDate()));
    }
}
