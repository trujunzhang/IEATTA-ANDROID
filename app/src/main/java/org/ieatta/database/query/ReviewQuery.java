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

    //  return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(new DBBuilder(), false);// for test

    final List<Realm> realmList = new LinkedList<>();

    public Task<List<IEAReviewsCellModel>> queryReview(String reviewRef, ReviewType type) {
        return new RealmModelReader<DBReview>(DBReview.class).fetchResults(
                new DBBuilder()
                        .whereEqualTo(AppConstant.kPAPFieldReviewRefKey, reviewRef)
                        .whereEqualTo(AppConstant.kPAPFieldReviewTypeKey, type.getType()),
                false, realmList).onSuccessTask(new Continuation<RealmResults<DBReview>, Task<RealmResults<DBTeam>>>() {
            @Override
            public Task<RealmResults<DBTeam>> then(Task<RealmResults<DBReview>> task) throws Exception {

                ReviewQuery.this.reviews = task.getResult();
                List<String> list = getTeamsList(task.getResult());

                if (list.size() == 0)
                    return Task.forResult(null);

                DBBuilder builder = new DBBuilder().whereContainedIn(AppConstant.kPAPFieldObjectUUIDKey, list);
                return new RealmModelReader<DBTeam>(DBTeam.class).fetchResults(builder, false, realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBTeam>, Task<List<IEAReviewsCellModel>>>() {
            @Override
            public Task<List<IEAReviewsCellModel>> then(Task<RealmResults<DBTeam>> task) throws Exception {
                List<IEAReviewsCellModel> list = new LinkedList<>();
                if (task.getResult() == null)
                    return Task.forResult(list);

                list = DBConvert.toReviewsCellModels(ReviewQuery.this.reviews, task.getResult());
                LocalDatabaseQuery.closeRealmList(realmList);
                return Task.forResult(list);
            }
        });
    }

    private List<String> getTeamsList(RealmResults<DBReview> reviews) {
        List<String> list = new LinkedList<>();
        for (DBReview review : reviews) {
            list.add(review.getUserRef());
        }
        return list;
    }

}
