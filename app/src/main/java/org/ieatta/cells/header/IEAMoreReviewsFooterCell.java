package org.ieatta.cells.header;

import android.view.View;
import android.widget.Button;


import android.view.View;
import android.widget.TextView;

import com.tableview.adapter.IEAViewHolder;
import com.tableview.adapter.enums.ViewHolderType;
import com.tableview.storage.models.CellType;

import org.ieatta.R;
import org.ieatta.cells.model.SectionMoreReviewsFooterCellModel;
import org.ieatta.cells.model.SectionTitleCellModel;

import com.tableview.adapter.IEAViewHolder;

import bolts.Continuation;
import bolts.Task;

public class IEAMoreReviewsFooterCell extends IEAViewHolder {
    private SectionMoreReviewsFooterCellModel model;

    public static CellType getType() {
        return new CellType(IEAMoreReviewsFooterCell.class, R.layout.businesspage_section_footer);
    }

    @Override
    public ViewHolderType getViewHolderType() {
        return ViewHolderType.footer;
    }


    private Button footerLargeButton;

    public IEAMoreReviewsFooterCell(View itemView) {
        super(itemView);

        this.footerLargeButton = (Button) itemView.findViewById(R.id.footer_large_button);
        this.footerLargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                this.model.viewController.performSegueForSeeReviews();
            }
        });
    }

    @Override
    public void render(Object value) {
        this.model = (SectionMoreReviewsFooterCellModel) value;


    }

    private void configureButton(int reviewsCount) {
//        String buttonTitle = EnvironmentUtils.sharedInstance.getGlobalContext().getResources().getString(R.string.No_More_Reviews);
//        this.footerLargeButton.setEnabled(true);
//        if (reviewsCount > 0) {
//            String moreReviews = EnvironmentUtils.sharedInstance.getGlobalContext().getResources().getString(R.string.See_More_Reviews);
//            buttonTitle = reviewsCount + " " + moreReviews;
//        } else {
//            this.footerLargeButton.setEnabled(false);
//        }
//
//        this.footerLargeButton.setText(buttonTitle);
    }

}
