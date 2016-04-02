package org.ieatta.cells.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.EditCellModel;
import org.ieatta.provide.IEAEditKey;

public class IEAEditTextFieldCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAEditTextFieldCell.class, R.layout.edit_text_field_cell);
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.None;
    }

    private EditText editText;
    private EditCellModel model;

    public IEAEditTextFieldCell(View itemView) {
        super(itemView);

        this.editText = (EditText) itemView.findViewById(R.id.editText);
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
        this.model = (EditCellModel) value;

        this.editText.setText(this.model.editValue);
        this.editText.setHint(this.model.editPlaceHolderResId);

        if (this.model.editKey == IEAEditKey.recipe_price) {
            EditTextLocker decimalEditTextLocker = new EditTextLocker(this.editText);
            decimalEditTextLocker.limitFractionDigitsinDecimal(2);
        }
    }


}
