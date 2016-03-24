package org.ieatta.provide;


import android.app.Activity;
import android.app.Fragment;

import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.fragments.EventDetailFragment;
import org.ieatta.activity.fragments.NearRestaurantsFragment;
import org.ieatta.activity.fragments.OrderedRecipesFragment;
import org.ieatta.activity.fragments.PageFragment;
import org.ieatta.activity.fragments.RecipeDetailFragment;
import org.ieatta.activity.fragments.RestaurantDetailFragment;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.parse.AppConstant;
import org.ieatta.tasks.EventDetailTask;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.tasks.NearRestaurantsTask;
import org.ieatta.tasks.OrderedRecipesTask;
import org.ieatta.tasks.RecipeDetailTask;
import org.ieatta.tasks.RestaurantDetailTask;


public enum MainSegueIdentifier {

    Unspecified(-1,""),

    // Main fragment.
    nearbyRestaurants(AppConstant.SOURCE_NEARBY_RESTAURANTS,"nearbyRestaurant"),

//    // Four menus in the near restaurant page.
//    editRestaurantSegueIdentifier("addEditRestaurant"),
//    searchRestaurantSegueIdentifier("searchRestaurant"),
//    managerPeopleSegueIdentifier("managerPeople"),
//    readReviewsSegueIdentifier("addEditRestaurant"),
//
//    // Four detail pages.
    detailRestaurantSegueIdentifier(AppConstant.SOURCE_RESTAURANT_DETAIL,"detailRestaurant"),
    detailEventSegueIdentifier(AppConstant.SOURCE_EVENT_DETAIL,"detailEvent"),
    detailOrderedRecipesSegueIdentifier(AppConstant.SOURCE_ORDERED_RECIPES,"detailOrderedRecipes"),
    detailRecipeSegueIdentifier(AppConstant.SOURCE_RECIPE_DETAIL,"detailRecipe"),
//
//    // Show all posted reviews for restaurant,recipe.
//    detailSeeReviewSegueIdentifier("seeReviewsInDetail"),

    // Show detail review from review list.


//    // Four new/edit model pages.(the following three, and restaurant)
//    editEventSegueIdentifier("addEditEvent"),
//    editPeopleSegueIdentifier("addEditPeople"),
//    editRecipeSegueIdentifier("addEditRecipe"),
//    // Choice Person in the event page.
//    choicePeopleSegueIdentifier("choicePeople"),
//
//    // Show detail review from review list.
//    postReviewSegueIdentifier("postReview"),

    photoPagesControllerSegueIdentifier(100,"photoPagesController");


    private final String name;
    private final int type;

    MainSegueIdentifier(int type,String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public static FragmentTask getFragment(HistoryEntry entry, Activity activity, PageViewModel model){
        switch (entry.getSource()){
            case AppConstant.SOURCE_NEARBY_RESTAURANTS:
                return new NearRestaurantsTask(entry, activity,model);
            case AppConstant.SOURCE_RESTAURANT_DETAIL:
                return new RestaurantDetailTask(entry, activity,model);
            case AppConstant.SOURCE_EVENT_DETAIL:
                return new EventDetailTask(entry, activity,model);
            case AppConstant.SOURCE_ORDERED_RECIPES:
                return new OrderedRecipesTask(entry, activity,model);
            case AppConstant.SOURCE_RECIPE_DETAIL:
                return new RecipeDetailTask(entry, activity,model);
        }

        return null;
    }


}
