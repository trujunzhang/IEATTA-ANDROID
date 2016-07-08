package org.ieatta.cells;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.RecipeModel;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.views.AvatarView;
import org.ieatta.views.RatingImageView;
import org.wikipedia.views.GoneIfEmptyTextView;

public class IEAOrderedRecipeCardCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAOrderedRecipeCardCell.class, R.layout.cell_ordered_recipe_card);
    }

    private AvatarView avatarView;

    private TextView displayNameLabel;
    private GoneIfEmptyTextView priceLabel;
    private RatingImageView ratingImageView;

    public IEAOrderedRecipeCardCell(View itemView) {
        super(itemView);

        this.avatarView = (AvatarView) itemView.findViewById(R.id.recipe_pictures);
        this.ratingImageView = (RatingImageView) itemView.findViewById(R.id.recipe_rating_image_view);
        this.displayNameLabel = (TextView) itemView.findViewById(R.id.recipe_name_text);
        this.priceLabel = (GoneIfEmptyTextView) itemView.findViewById(R.id.recipe_price_text);
    }

    private void setRecipeModel(RecipeModel model) {
        this.avatarView.loadNewPhotoByModel(model.recipeUUID);
        this.ratingImageView.queryRatingInReviewsByModel(model.recipeUUID, ReviewType.Review_Recipe);

        this.displayNameLabel.setText(model.recipeName);
        this.priceLabel.setText(model.priceValue);
    }

    @Override
    public void render(Object value) {
        this.setRecipeModel((RecipeModel) value);
    }
}
