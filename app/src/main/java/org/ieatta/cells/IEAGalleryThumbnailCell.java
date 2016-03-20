package org.ieatta.cells;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.IEANearRestaurantMore;

public class IEAGalleryThumbnailCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAGalleryThumbnailCell.class, R.layout.cell_gallerythumbnail);
    }


    public IEAGalleryThumbnailCell(View itemView) {
        super(itemView);

    }

    @Override
    public void render(Object value) {

    }
}
