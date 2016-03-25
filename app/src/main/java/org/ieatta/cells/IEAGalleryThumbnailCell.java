package org.ieatta.cells;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.gallery.GalleryThumbnailScrollView;
import org.ieatta.cells.model.IEAGalleryThumbnail;
import org.ieatta.cells.model.IEANearRestaurantMore;

public class IEAGalleryThumbnailCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAGalleryThumbnailCell.class, R.layout.cell_gallerythumbnail);
    }

    private GalleryThumbnailScrollView thumbnailGallery;

    public IEAGalleryThumbnailCell(View itemView) {
        super(itemView);

        thumbnailGallery = (GalleryThumbnailScrollView) itemView.findViewById(R.id.link_preview_thumbnail_gallery);
    }

    @Override
    public void render(Object value) {
        IEAGalleryThumbnail galleryThumbnail = (IEAGalleryThumbnail) value;

        this.setGalleryResult(galleryThumbnail.getResult());
        this.thumbnailGallery.setGalleryViewListener(galleryThumbnail.galleryViewListener);
    }

    private void setGalleryResult(GalleryCollection result) {
//        if (result.getItemList().size() > 2) {
            thumbnailGallery.setGalleryCollection(result);

            // When the visibility is immediately changed, the images flicker. Add a short delay.
            final int animationDelayMillis = 100;
            thumbnailGallery.postDelayed(new Runnable() {
                @Override
                public void run() {
                    thumbnailGallery.setVisibility(View.VISIBLE);
                }
            }, animationDelayMillis);
//        }
    }
}
