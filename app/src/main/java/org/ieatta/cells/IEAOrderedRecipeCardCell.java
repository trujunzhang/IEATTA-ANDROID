package org.ieatta.cells;

import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.IEAApp;
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
    private TextView priceLabel;
    private RatingImageView ratingImageView;
    private ImageView recipeToolbar;

    public IEAOrderedRecipeCardCell(View itemView) {
        super(itemView);

        this.avatarView = (AvatarView) itemView.findViewById(R.id.recipe_pictures);
        this.ratingImageView = (RatingImageView) itemView.findViewById(R.id.recipe_rating_image_view);
        this.displayNameLabel = (TextView) itemView.findViewById(R.id.recipe_name_text);
        this.priceLabel = (TextView) itemView.findViewById(R.id.recipe_price_text);
        this.recipeToolbar = (ImageView) itemView.findViewById(R.id.recipe_toolbar);

        PopupMenu popup = new PopupMenu(IEAApp.getInstance().getApplicationContext(), this.recipeToolbar);
        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.recipe_card_toolbar);

        this.recipeToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setRecipeModel(RecipeModel model) {
        this.avatarView.loadLeadImage(model.recipeUUID, model.task);
        this.ratingImageView.queryRatingInReviewsByModel(model.recipeUUID, ReviewType.Review_Recipe, model.task);

        this.displayNameLabel.setText(model.recipeName);
        this.priceLabel.setText(model.priceValue);
    }

    @Override
    public void render(Object value) {
        this.setRecipeModel((RecipeModel) value);
    }
}
