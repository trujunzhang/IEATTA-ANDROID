package org.ieatta.provide;



public enum MainSegueIdentifier {

    Unspecified("", null);

//    // Four menus in the near restaurant page.
//    editRestaurantSegueIdentifier("addEditRestaurant", IEAEditRestaurantViewController.class),
//
//    searchRestaurantSegueIdentifier("searchRestaurant", IEASearchRestaurantViewController.class),
//    managerPeopleSegueIdentifier("managerPeople", IEAManagerPeopleViewController.class),
//    readReviewsSegueIdentifier("addEditRestaurant", IEAReadReviewsViewController.class),
//    // Four detail pages.
//    detailRestaurantSegueIdentifier("detailRestaurant", IEARestaurantDetailViewController.class),
//    detailEventSegueIdentifier("detailEvent", IEAEventDetailViewController.class),
//    detailOrderedRecipesSegueIdentifier("detailOrderedRecipes", IEAOrderedRecipesViewController.class),
//    detailRecipeSegueIdentifier("detailRecipe", IEARecipeDetailViewController.class),
//
//    // Show all posted reviews for restaurant,recipe.
//    detailSeeReviewSegueIdentifier("seeReviewsInDetail", IEASeeReviewsInDetailViewController.class),
//
//    // Show detail review from review list.
//    detailReviewSegueIdentifier("detailReview", IEAReviewDetailViewController.class),
//
//    // Four new/edit model pages.(the following three, and restaurant)
//    editEventSegueIdentifier("addEditEvent", IEAEditEventViewController.class),
//    editPeopleSegueIdentifier("addEditPeople", IEAEditPeopleViewController.class),
//    editRecipeSegueIdentifier("addEditRecipe", IEAEditRecipeViewController.class),
//    // Choice Person in the event page.
//    choicePeopleSegueIdentifier("choicePeople", IEAChoicePeopleViewController.class),
//
//    // Show detail review from review list.
//    postReviewSegueIdentifier("postReview", IEAWriteReviewViewController.class),
//
//    photoPagesControllerSegueIdentifier("photoPagesController", PhotoGalleryPagerActivity.class);


    private String name;
    private Class<?> activity;

    MainSegueIdentifier(String name, Class<?> activity) {
        this.name = name;
        this.activity = activity;
    }

    public Class<?> getActivity() {
        return this.activity;
    }

}
