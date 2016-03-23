package org.ieatta.database.query;

import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.AppConstant;

import bolts.Task;
import io.realm.RealmResults;

public class RecipeQuery {
    private RealmResults<DBRecipe> recipes;

    public Task<Long> queryOrderedRecipesCount(String teamUUID, String eventUUID) {
        return new RealmModelReader<DBRecipe>(DBRecipe.class).getCountObjects(
                new DBBuilder()
                        .whereEqualTo(AppConstant.kPAPFieldOrderedPeopleRefKey, teamUUID)
                        .whereEqualTo(AppConstant.kPAPFieldEventRefKey, eventUUID));
    }

}
