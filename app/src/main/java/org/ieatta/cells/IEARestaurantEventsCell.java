package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.database.models.DBEvent;

public class IEARestaurantEventsCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEARestaurantEventsCell.class, R.layout.cell_restaurant_events);
    }

    private IEARestaurantEventsCell self = this;

    private TextView infoLabel;
    private TextView timeInfoLabel;
    private TextView timeAgoLabelLabel;

    public IEARestaurantEventsCell(View itemView) {
        super(itemView);

        self.infoLabel = (TextView) itemView.findViewById(R.id.titleTextView);
        self.timeInfoLabel = (TextView) itemView.findViewById(R.id.infoTextView);
        self.timeAgoLabelLabel = (TextView) itemView.findViewById(R.id.timeAgoTextView);
    }

    @Override
    public void render(Object value) {
        DBEvent more = (DBEvent) value;

        self.infoLabel.setText(more.getDisplayName());
//        if (more.waiter == null || more.waiter.equals("")) {
//            self.timeInfoLabel.setText(R.string.No_waiters_servered_for_you);
//        } else {
//            self.timeInfoLabel.setText(more.waiter);
//        }

//        self.timeAgoLabelLabel.setText(more.getTimeAgoString());

    }
}
