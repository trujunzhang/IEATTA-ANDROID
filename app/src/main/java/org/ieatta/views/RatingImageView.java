package org.ieatta.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.tasks.OrderedRecipesTask;

import bolts.Continuation;
import bolts.Task;

public class RatingImageView extends ImageView {
    private int reviewRating = 0;

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

    /**
     * @param reviewRef          model's UUID
     * @param reviewType         review's type
     * @param orderedRecipesTask OrderedRecipesTask's instance
     */
    public void queryRatingInReviewsByModel(final String reviewRef, ReviewType reviewType, final OrderedRecipesTask orderedRecipesTask) {
        this.reviewRating = orderedRecipesTask.getRating(reviewRef);
        if (this.reviewRating != -1) {
            this.setImageLevel(this.reviewRating);
        } else {
            this.setVisibility(GONE);
            this.reviewRating = 0;
            LocalDatabaseQuery.queryRatingInReviews(reviewRef, reviewType).onSuccess(new Continuation<Integer, Void>() {
                @Override
                public Void then(Task<Integer> task) throws Exception {
                    int rating = task.getResult();
                    RatingImageView.this.reviewRating = rating;
                    orderedRecipesTask.setRating(reviewRef, rating);
                    return null;
                }
            }).continueWith(new Continuation<Void, Object>() {
                @Override
                public Object then(Task<Void> task) throws Exception {
                    RatingImageView.this.setVisibility(VISIBLE);
                    RatingImageView.this.setImageLevel(RatingImageView.this.reviewRating);
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }
    }


}
