package org.ieatta.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;

import bolts.Continuation;
import bolts.Task;

public class RatingImageView extends ImageView {
    public RatingImageView(Context context) {
        super(context);
    }

    public RatingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void queryRatingInReviewsByModel(String reviewRef, ReviewType reviewType) {
        this.setImageLevel(0);
        LocalDatabaseQuery.queryRatingInReviews(reviewRef, reviewType).onSuccess(new Continuation<Integer, Void>() {
            @Override
            public Void then(Task<Integer> task) throws Exception {
                int rating = task.getResult();
                RatingImageView.this.setImageLevel(rating);
                return null;
            }
        });
    }


}
