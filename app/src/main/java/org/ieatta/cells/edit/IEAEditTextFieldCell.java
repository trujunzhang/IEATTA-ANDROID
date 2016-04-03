package org.ieatta.cells.edit;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.cells.model.EditCellModel;
import org.ieatta.provide.IEAEditKey;

public class IEAEditTextFieldCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAEditTextFieldCell.class, R.layout.cell_edit_text_field);
    }

    @Override
    protected boolean shouldOnClickItem() {
        return false;
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.None;
    }

    private EditText editText;
    private TextInputLayout floatLabeledEditText;
    private EditCellModel model;

    public IEAEditTextFieldCell(View itemView) {
        super(itemView);

        this.floatLabeledEditText = (TextInputLayout) itemView.findViewById(R.id.text_input_message);
        this.editText = (EditText) itemView.findViewById(R.id.message);
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
        this.floatLabeledEditText.setHint(IEAApp.getInstance().getResources().getString(this.model.editPlaceHolderResId));

        if (this.model.editKey == IEAEditKey.recipe_price) {
            EditTextLocker decimalEditTextLocker = new EditTextLocker(this.editText);
            decimalEditTextLocker.limitFractionDigitsinDecimal(2);
        }
    }


}
