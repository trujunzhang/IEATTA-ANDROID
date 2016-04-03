package org.ieatta.database.query;

import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.AppConstant;
import org.ieatta.tasks.DBConvert;

import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.Realm;
import io.realm.RealmResults;

public class ReviewQuery {
    private RealmResults<DBReview> reviews;

    public void setReviews(RealmResults<DBReview> reviews) {
        this.reviews = reviews;
        this.reviewsCount = reviews.size();
    }

    //  return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(new DBBuilder(), false);// for test

    final List<Realm> realmList = new LinkedList<>();

    public int reviewsCount = 0;
    public int ratingReview = 0;

    public Task<List<IEAReviewsCellModel>> queryReview(String reviewRef, ReviewType type, final int limit) {
        DBBuilder reviewBuilder = new DBBuilder()
                .whereEqualTo(AppConstant.kPAPFieldReviewRefKey, reviewRef)
                .whereEqualTo(AppConstant.kPAPFieldReviewTypeKey, type.getType());

        if (limit != AppConstant.limit_reviews_no)
            reviewBuilder = reviewBuilder.setLimit(limit);

        return new RealmModelReader<DBReview>(DBReview.class).fetchResults(reviewBuilder, false, realmList).onSuccessTask(new Continuation<RealmResults<DBReview>, Task<RealmResults<DBTeam>>>() {
            @Override
            public Task<RealmResults<DBTeam>> then(Task<RealmResults<DBReview>> task) throws Exception {
                ReviewQuery.this.setReviews(task.getResult());

                if (ReviewQuery.this.reviewsCount == 0)
                    return Task.forResult(null);

                makeRatingReview(task.getResult());

                DBBuilder builder = new DBBuilder().whereContainedIn(AppConstant.kPAPFieldObjectUUIDKey, getTeamsList(task.getResult(), limit));
                return new RealmModelReader<DBTeam>(DBTeam.class).fetchResults(builder, false, realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBTeam>, Task<List<IEAReviewsCellModel>>>() {
            @Override
            public Task<List<IEAReviewsCellModel>> then(Task<RealmResults<DBTeam>> task) throws Exception {

                List<IEAReviewsCellModel> list = new LinkedList<IEAReviewsCellModel>();
                // If when reviews count is zero, task's result return null.
                // We return an empty list.
                if (task.getResult() == null)
                    return Task.forResult(list);

                list = DBConvert.toReviewsCellModels(ReviewQuery.this.reviews, task.getResult());
                LocalDatabaseQuery.closeRealmList(realmList);
                return Task.forResult(list);
            }
        });
    }

    private void makeRatingReview(RealmResults<DBReview> result) {
        int count = result.size();
        int sum = 0;
        for(DBReview item : result){
            sum +=item.getRate();
        }

        this.ratingReview = sum/count;
    }

    private List<String> getTeamsList(RealmResults<DBReview> reviews, int limit) {
        int step = 0;
        List<String> list = new LinkedList<>();
        for (DBReview review : reviews) {
            if (limit != AppConstant.limit_reviews_no && step >= limit)
                break;
            list.add(review.getUserRef());
            step++;
        }
        return list;
    }

}
