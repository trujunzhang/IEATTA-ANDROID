package org.ieatta.database.query;

import org.ieatta.cells.model.ReviewsCellModel;
import org.ieatta.database.models.DBRecipe;
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

public class RecipeQuery {
    private RealmResults<DBRecipe> recipes;

    public Task<Integer> queryOrderedRecipesCount(String teamUUID, String eventUUID) {
        return new RealmModelReader<DBRecipe>(DBRecipe.class).fetchResults(
                new DBBuilder()
                        .whereEqualTo(DBConstant.kPAPFieldOrderedPeopleRefKey, teamUUID)
                        .whereEqualTo(DBConstant.kPAPFieldEventRefKey, eventUUID),
                false).onSuccessTask(new Continuation<RealmResults<DBRecipe>, Task<Integer>>() {
            @Override
            public Task<Integer> then(Task<RealmResults<DBRecipe>> task) throws Exception {
                RecipeQuery.this.recipes = task.getResult();

                return Task.forResult(0);
            }
        });
    }

}
