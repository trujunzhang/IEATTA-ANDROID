package org.ieatta.cells.model;

import org.ieatta.database.models.DBRecipe;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.tasks.OrderedRecipesTask;

public class RecipeModel extends EditBaseCellModel {

    public final String recipeName;
    public final String recipeUUID;
    public final String priceValue;
    public final OrderedRecipesTask task;

    public RecipeModel(DBRecipe review, OrderedRecipesTask task) {
        super(IEAEditKey.Unknow);

        this.recipeName = review.getDisplayName();
        this.recipeUUID = review.getUUID();
        this.priceValue = String.format("$ %s", review.getPrice());

        this.task = task;
    }
}
