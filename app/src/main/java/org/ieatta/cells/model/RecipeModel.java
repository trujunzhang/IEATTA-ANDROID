package org.ieatta.cells.model;

import org.ieatta.database.models.DBRecipe;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.FragmentTask;

public class RecipeModel extends EditBaseCellModel {

    private final String title;
    private final String recipeUUID;
    private final String priceValue;
    private final FragmentTask task;

    public RecipeModel(DBRecipe review, FragmentTask task) {
        super(IEAEditKey.Unknow);

        this.title = review.getDisplayName();
        this.recipeUUID = review.getUUID();
        this.priceValue = review.getPrice();

        this.task = task;
    }
}
