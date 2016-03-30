package org.ieatta.cells.edit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;

public class IEAEditTextRecipeFieldCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAEditTextRecipeFieldCell.class, R.layout.edit_text_recipe_field_cell);
    }

    private IEAEditTextRecipeFieldCell self = this;

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.None;
    }

    private EditText editText;
    private EditCellModel model;

    public IEAEditTextRecipeFieldCell(View itemView) {
        super(itemView);

      this.editText = (EditText) itemView.findViewById(R.id.editText);
      this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              this.model.editValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void render(Object value) {
      this.model = (EditCellModel) value;
      this.editText.setText(self.model.editValue);
      this.editText.setHint(self.model.editPlaceHolderResId);

        if (self.model.editKey == IEAEditKey.recipe_price) {
            EditTextLocker decimalEditTextLocker = new EditTextLocker(self.editText);
            decimalEditTextLocker.limitFractionDigitsinDecimal(2);
        }
    }


}
