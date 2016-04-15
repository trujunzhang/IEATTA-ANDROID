package org.ieatta.provide;


import android.app.Activity;

import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.parse.AppConstant;
import org.ieatta.tasks.EventDetailTask;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.tasks.NearRestaurantsTask;
import org.ieatta.tasks.OrderedRecipesTask;
import org.ieatta.tasks.RecipeDetailTask;
import org.ieatta.tasks.RestaurantDetailTask;
import org.ieatta.tasks.edit.EventEditTask;
import org.ieatta.tasks.edit.PeopleEditTask;
import org.ieatta.tasks.edit.RecipeEditTask;
import org.ieatta.tasks.edit.RestaurantEditTask;


public enum MainSegueIdentifier {

    Unspecified(-1, ""),

    // Main fragment.
    nearbyRestaurants(AppConstant.SOURCE_NEARBY_RESTAURANTS, "nearbyRestaurant"),

//    // Four menus in the near restaurant page.
//    searchRestaurantSegueIdentifier("searchRestaurant"),
//    managerPeopleSegueIdentifier("managerPeople"),
//    readReviewsSegueIdentifier("addEditRestaurant"),

    // Four detail pages.
    detailRestaurantSegueIdentifier(AppConstant.SOURCE_RESTAURANT_DETAIL, "detailRestaurant"),
    detailEventSegueIdentifier(AppConstant.SOURCE_EVENT_DETAIL, "detailEvent"),
    detailOrderedRecipesSegueIdentifier(AppConstant.SOURCE_ORDERED_RECIPES, "detailOrderedRecipes"),
    detailRecipeSegueIdentifier(AppConstant.SOURCE_RECIPE_DETAIL, "detailRecipe"),

    // Show all posted reviews for restaurant,recipe.
//    detailSeeReviewSegueIdentifier("seeReviewsInDetail"),

    // Show detail review from review list.


    // Four new/edit model pages.(the following three, and restaurant)
    editRestaurantSegueIdentifier(AppConstant.SOURCE_RESTAURANT_EDIT, "addEditRestaurant"),
    editEventSegueIdentifier(AppConstant.SOURCE_EVENT_EDIT, "addEditEvent"),
    editPeopleSegueIdentifier(AppConstant.SOURCE_PEOPLE_EDIT, "addEditPeople"),
    editRecipeSegueIdentifier(AppConstant.SOURCE_RECIPE_EDIT, "addEditRecipe"),

    // Choice Person in the event page.
    choicePeopleSegueIdentifier(AppConstant.SOURCE_CHOICE_PEOPLE, "choicePeople"),

    // Show detail review from review list.
//    postReviewSegueIdentifier("postReview"),

    photoPagesControllerSegueIdentifier(100, "photoPagesController");


    private final String name;
    private final int type;

    MainSegueIdentifier(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public static FragmentTask getFragment(HistoryEntry entry, Activity activity, PageViewModel model) {
        switch (entry.getSource()) {
            // Four detail pages.
            case AppConstant.SOURCE_NEARBY_RESTAURANTS:
                return new NearRestaurantsTask(entry, activity, model);
            case AppConstant.SOURCE_RESTAURANT_DETAIL:
                return new RestaurantDetailTask(entry, activity, model);
            case AppConstant.SOURCE_EVENT_DETAIL:
                return new EventDetailTask(entry, activity, model);
            case AppConstant.SOURCE_ORDERED_RECIPES:
                return new OrderedRecipesTask(entry, activity, model);
            case AppConstant.SOURCE_RECIPE_DETAIL:
                return new RecipeDetailTask(entry, activity, model);
            // Four new/edit model pages.(the following three, and restaurant)
            case AppConstant.SOURCE_RESTAURANT_EDIT:
                return new RestaurantEditTask(entry, activity, model);
            case AppConstant.SOURCE_EVENT_EDIT:
                return new EventEditTask(entry, activity, model);
            case AppConstant.SOURCE_PEOPLE_EDIT:
                return new PeopleEditTask(entry, activity, model);
            case AppConstant.SOURCE_RECIPE_EDIT:
                return new RecipeEditTask(entry, activity, model);
            case AppConstant.SOURCE_CHOICE_PEOPLE:
                return new RecipeEditTask(entry, activity, model);
        }

        return null;
    }


}
