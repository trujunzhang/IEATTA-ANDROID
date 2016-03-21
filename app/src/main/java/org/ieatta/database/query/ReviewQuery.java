package org.ieatta.database.query;

import org.ieatta.cells.model.ReviewsCellModel;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.DBConstant;
import org.ieatta.tasks.DBConvert;

import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class ReviewQuery {
    public RealmResults<DBReview> reviews;
    public List<ReviewsCellModel> reviewsCellModelList;

    //  return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(new DBBuilder(), false);// for test

    public Task<List<ReviewsCellModel>> queryReview(String reviewRef,ReviewType type){
        return new RealmModelReader<DBReview>(DBReview.class).fetchResults(
                new DBBuilder()
                        .whereEqualTo(DBConstant.kPAPFieldReviewRefKey, reviewRef)
                        .whereEqualTo(DBConstant.kPAPFieldReviewTypeKey, type.getType()),
                false).onSuccessTask(new Continuation<RealmResults<DBReview>, Task<RealmResults<DBTeam>>>() {
            @Override
            public Task<RealmResults<DBTeam>> then(Task<RealmResults<DBReview>> task) throws Exception {
                ReviewQuery.this.reviews = task.getResult();
                List<String> list = getTeamsList(task.getResult());

                return new RealmModelReader<DBTeam>(DBTeam.class).fetchResults(
                        new DBBuilder().whereContainedIn(DBConstant.kPAPFieldObjectUUIDKey, list), false);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBTeam>, Task<List<ReviewsCellModel>>>() {
            @Override
            public Task<List<ReviewsCellModel>> then(Task<RealmResults<DBTeam>> task) throws Exception {
                RealmResults<DBTeam> teams = task.getResult();
                List<ReviewsCellModel> value = DBConvert.toReviewsCellModels(ReviewQuery.this.reviews, teams);
                return Task.forResult(value);
            }
        });
    }

    private List<String> getTeamsList(RealmResults<DBReview> reviews){
        List<String> list = new LinkedList<>();
        for (DBReview review:reviews) {
            list.add(review.getUserRef());
        }
        return list;
    }

}
