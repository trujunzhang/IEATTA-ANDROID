package org.ieatta.provide;


public enum MainSegueIdentifier {

    Unspecified(""),

    // Four menus in the near restaurant page.
    editRestaurantSegueIdentifier("addEditRestaurant"),
    searchRestaurantSegueIdentifier("searchRestaurant"),
    managerPeopleSegueIdentifier("managerPeople"),
    readReviewsSegueIdentifier("addEditRestaurant"),

    // Four detail pages.
    detailRestaurantSegueIdentifier("detailRestaurant"),
    detailEventSegueIdentifier("detailEvent"),
    detailOrderedRecipesSegueIdentifier("detailOrderedRecipes"),
    detailRecipeSegueIdentifier("detailRecipe"),

    // Show all posted reviews for restaurant,recipe.
    detailSeeReviewSegueIdentifier("seeReviewsInDetail"),

    // Show detail review from review list.
    detailReviewSegueIdentifier("detailReview"),

    // Four new/edit model pages.(the following three, and restaurant)
    editEventSegueIdentifier("addEditEvent"),
    editPeopleSegueIdentifier("addEditPeople"),
    editRecipeSegueIdentifier("addEditRecipe"),
    // Choice Person in the event page.
    choicePeopleSegueIdentifier("choicePeople"),

    // Show detail review from review list.
    postReviewSegueIdentifier("postReview"),

    photoPagesControllerSegueIdentifier("photoPagesController");


    private String name;

    MainSegueIdentifier(String name) {
        this.name = name;
    }




}
