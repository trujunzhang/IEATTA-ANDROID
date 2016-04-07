package org.ieatta.cells.edit;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.DatePickerCellModel;
import org.ieatta.provide.IEAEditKey;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IEADatePickerCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEADatePickerCell.class, R.layout.date_picker_cell);
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.None;
    }

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy, HH:mm aa");
    private boolean isDialog = false;

    private Date editedDate;

    private TextView editText;
    private TextView titleTextView;

    private Button clickedView;

    private DatePickerCellModel model;

    public IEADatePickerCell(View itemView) {
        super(itemView);

        this.clickedView = (Button) itemView.findViewById(R.id.clicked_view);
        this.titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        this.editText = (TextView) itemView.findViewById(R.id.editText);

        this.clickedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDialog == true) {
                    return;
                }

                new SlideDateTimePicker.Builder(null)
                        .setListener(listener)
                        .setInitialDate(editedDate)
                                //.setMinDate(minDate)
                                //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
//                                .setTheme(SlideDateTimePicker.HOLO_DARK)
//                                .setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });
    }

    @Override
    public void render(Object value) {
        this.model = (DatePickerCellModel) value;

        if (this.model.editKey == IEAEditKey.event_starttime) {
            this.titleTextView.setText(R.string.Start_Time);
        } else {
            this.titleTextView.setText(R.string.End_Time);
        }

        this.reloadTableRow(this.model.date);
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            reloadTableRow(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
        }
    };

    private void reloadTableRow(Date date) {
        this.editedDate = date;

        // Cache for EditModel.
        this.model.date = date;

        this.editText.setText(dateFormatter.format(editedDate));
        isDialog = false;
    }
}