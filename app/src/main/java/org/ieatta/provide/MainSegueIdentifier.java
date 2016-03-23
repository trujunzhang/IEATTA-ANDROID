package org.ieatta.provide;


import android.app.Fragment;

import org.ieatta.activity.fragments.NearRestaurantsFragment;
import org.ieatta.activity.fragments.PageFragment;
import org.ieatta.parse.AppConstant;


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
//    detailRestaurantSegueIdentifier("detailRestaurant"),
//    detailEventSegueIdentifier("detailEvent"),
//    detailOrderedRecipesSegueIdentifier("detailOrderedRecipes"),
//    detailRecipeSegueIdentifier("detailRecipe"),
//
//    // Show all posted reviews for restaurant,recipe.
//    detailSeeReviewSegueIdentifier("seeReviewsInDetail"),
//
//    // Show detail review from review list.
//    detailReviewSegueIdentifier("detailReview"),
//
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

    public static PageFragment getFragment(int source){
        switch (source){
            case AppConstant.SOURCE_NEARBY_RESTAURANTS:
                return new NearRestaurantsFragment();
        }

        return null;
    }


}
