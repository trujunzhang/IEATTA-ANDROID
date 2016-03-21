package org.ieatta.cells;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.ReviewsCellModel;
import org.ieatta.views.AvatarView;

public class IEAReviewsCell extends IEAViewHolder {
    public static CellType getType() {
        return new CellType(IEAReviewsCell.class, R.layout.see_reviews_cell);
    }

    private AvatarView avatarView;

    private TextView titleLabel;
    private TextView timeAgoTextView;

    private ImageView business_review_star_rating;
    private TextView reviewContentLabel;

    public IEAReviewsCell(View itemView) {
        super(itemView);

        this.avatarView = (AvatarView) itemView.findViewById(R.id.avatarView);
        this.titleLabel = (TextView) itemView.findViewById(R.id.titleTextView);
        this.timeAgoTextView = (TextView) itemView.findViewById(R.id.timeAgoTextView);

        this.business_review_star_rating = (ImageView) itemView.findViewById(R.id.business_review_star_rating);
        this.reviewContentLabel = (TextView) itemView.findViewById(R.id.reviewContentLabel);
    }

    @Override
    public void render(Object value) {
        ReviewsCellModel model = (ReviewsCellModel) value;

        this.titleLabel.setText(model.title);
        this.timeAgoTextView.setText(model.timeAgoString);

//        this.avatarView.loadNewPhotoByModel(model.user, R.drawable.blank_user_small);

        this.business_review_star_rating.setImageLevel(model.ratingValue);
        this.reviewContentLabel.setText(model.reviewContent);
    }
}
