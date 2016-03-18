package org.ieatta.cells.header;

import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.IEARestaurantDetailHeader;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.views.RatingImageView;

public class IEARestaurantDetailHeaderCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEARestaurantDetailHeaderCell.class, R.layout.restaurant_detail_header_cell);
    }

    private IEARestaurantDetailHeaderCell self = this;

    private IEARestaurantDetailHeader model;

    private TextView displayNameLabel;
    private RatingImageView ratingImageView;

    private TextView editButton;
    private TextView firstButton;
    private TextView secondButton;
    private TextView thirdButton;

    public IEARestaurantDetailHeaderCell(View itemView) {
        super(itemView);

        self.displayNameLabel = (TextView) itemView.findViewById(R.id.displayNameTextView);
        self.ratingImageView = (RatingImageView) itemView.findViewById(R.id.business_review_star_rating);

        self.editButton = (TextView) itemView.findViewById(R.id.editNameTextView);
        self.editButton.setText(R.string.Edit_Restaurant);
        self.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                self.model.viewController.performSegueForEditingModel();
            }
        });
        self.firstButton = (TextView) itemView.findViewById(R.id.firstTextView);
        self.firstButton.setText(R.string.Add_Event);
        self.firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                self.model.viewController.performSegueForAddingEvent();
            }
        });
        self.secondButton = (TextView) itemView.findViewById(R.id.secondTextView);
        self.secondButton.setText(R.string.Write_Review);
        self.secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                self.model.viewController.performSegueForWritingReview();
            }
        });
        self.thirdButton = (TextView) itemView.findViewById(R.id.thirdTextView);
        self.thirdButton.setText(R.string.See_Reviews);
        self.thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                self.model.viewController.performSegueForSeeReviews();
            }
        });
    }

    @Override
    public void render(Object value) {
        self.model = (IEARestaurantDetailHeader) value;

        self.displayNameLabel.setText(self.model.model.getDisplayName());
//        self.ratingImageView.queryRatingInReviewsByModel(self.model.model);
    }
}
