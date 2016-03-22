package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.views.AvatarView;
import org.ieatta.views.RatingImageView;

public class IEAOrderedRecipeCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAOrderedRecipeCell.class, R.layout.cell_ordered_recipe);
    }

    private AvatarView avatarView;

    private TextView displayNameLabel;
    private TextView priceLabel;
    private RatingImageView ratingImageView;

    public IEAOrderedRecipeCell(View itemView) {
        super(itemView);

        this.avatarView = (AvatarView) itemView.findViewById(R.id.avatarView);
        this.displayNameLabel = (TextView) itemView.findViewById(R.id.titleTextView);
        this.priceLabel = (TextView) itemView.findViewById(R.id.priceTextView);
        this.ratingImageView = (RatingImageView) itemView.findViewById(R.id.business_review_star_rating);
    }

    @Override
    public void render(Object value) {
        DBRecipe model = (DBRecipe) value;
        this.displayNameLabel.setText(model.getDisplayName());
        this.priceLabel.setText("$ " + model.getPrice() + "");

//        this.ratingImageView.queryRatingInReviewsByModel(model);

        this.avatarView.loadNewPhotoByModel(model.getUUID());
    }
}
