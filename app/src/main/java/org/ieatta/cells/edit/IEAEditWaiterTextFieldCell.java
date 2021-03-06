package org.ieatta.cells.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;
import org.ieatta.R;
import org.ieatta.cells.model.EditWaiterCellModel;

public class IEAEditWaiterTextFieldCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAEditWaiterTextFieldCell.class, R.layout.edit_waiter_text_field_cell);
    }

    private IEAEditWaiterTextFieldCell self = this;

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.None;
    }

    private EditText editText;
    private EditWaiterCellModel model;

    public IEAEditWaiterTextFieldCell(View itemView) {
        super(itemView);

      this.editText = (EditText) itemView.findViewById(R.id.editText);
        itemView.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              model.viewController.takeAPhotoButtonTapped();
            }
        });
      this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              model.editValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void render(Object value) {
      this.model = (EditWaiterCellModel) value;

      this.editText.setText(self.model.editValue);
      this.editText.setHint(self.model.editPlaceHolderResId);
    }
}
