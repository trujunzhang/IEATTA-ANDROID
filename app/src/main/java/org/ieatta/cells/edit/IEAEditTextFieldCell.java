package org.ieatta.cells.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;

public class IEAEditTextFieldCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAEditTextFieldCell.class, R.layout.edit_text_field_cell);
    }

    private IEAEditTextFieldCell self = this;

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.None;
    }

    private EditText editText;
    private EditCellModel model;

    public IEAEditTextFieldCell(View itemView) {
        super(itemView);

        self.editText = (EditText) itemView.findViewById(R.id.editText);
        self.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                self.model.editValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void render(Object value) {
        self.model = (EditCellModel) value;
        self.editText.setText(self.model.editValue);
        self.editText.setHint(self.model.editPlaceHolderResId);

        if (self.model.editKey == IEAEditKey.recipe_price) {
            EditTextLocker decimalEditTextLocker = new EditTextLocker(self.editText);
            decimalEditTextLocker.limitFractionDigitsinDecimal(2);
        }
    }


}
