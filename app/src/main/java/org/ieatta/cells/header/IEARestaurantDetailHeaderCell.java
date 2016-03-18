package org.ieatta.cells.header;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.IEARestaurantDetailHeader;
import org.ieatta.views.RatingImageView;

public class IEARestaurantDetailHeaderCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEARestaurantDetailHeaderCell.class, R.layout.cell_header_restaurant_detail);
    }

    private IEARestaurantDetailHeader model;

    private TextView displayNameLabel;
    private RatingImageView ratingImageView;

    private TextView editButton;
    private TextView firstButton;
    private TextView secondButton;
    private TextView thirdButton;

    public IEARestaurantDetailHeaderCell(View itemView) {
        super(itemView);

        this.displayNameLabel = (TextView) itemView.findViewById(R.id.displayNameTextView);
        this.ratingImageView = (RatingImageView) itemView.findViewById(R.id.business_review_star_rating);

        this.editButton = (TextView) itemView.findViewById(R.id.editNameTextView);
        this.editButton.setText(R.string.Edit_Restaurant);
        this.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                this.model.viewController.performSegueForEditingModel();
            }
        });
        this.firstButton = (TextView) itemView.findViewById(R.id.firstTextView);
        this.firstButton.setText(R.string.Add_Event);
        this.firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                this.model.viewController.performSegueForAddingEvent();
            }
        });
        this.secondButton = (TextView) itemView.findViewById(R.id.secondTextView);
        this.secondButton.setText(R.string.Write_Review);
        this.secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                this.model.viewController.performSegueForWritingReview();
            }
        });
        this.thirdButton = (TextView) itemView.findViewById(R.id.thirdTextView);
        this.thirdButton.setText(R.string.See_Reviews);
        this.thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                this.model.viewController.performSegueForSeeReviews();
            }
        });
    }

    @Override
    public void render(Object value) {
        this.model = (IEARestaurantDetailHeader) value;

        this.displayNameLabel.setText(this.model.model.getDisplayName());
//        this.ratingImageView.queryRatingInReviewsByModel(this.model.model);
    }
}
