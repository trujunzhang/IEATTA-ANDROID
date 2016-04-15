package org.ieatta.parse;

import android.content.Intent;

import org.ieatta.database.models.DBTeam;

public final class AppConstant {
    public final static String kPAPFieldModelGEOHASH = "geoHash";

    public final static String kPAPFieldModelOnlineCreatedAtKey = "createdAt";

    // Common fields
    public static final String kPAPFieldObjectUUIDKey = "UUID";
    public static final String kPAPFieldObjectCreatedDateKey = "objectCreatedDate";
    public static final String kPAPFieldDisplayNameKey = "displayName";
    public static final String kPAPFieldFlagKey = "flag";

    // For NewRecord
    public static final String kPAPFieldModelTypeKey = "modelType";
    public static final String kPAPFieldModelPointKey = "modelPoint";
    
    // For Event
    public static final String kPAPFieldStartDateKey = "startDate";
    public static final String kPAPFieldEndDateKey = "endDate";
    public static final String kPAPFieldWhatToEatKey = "whatToEat";
    public static final String kPAPFieldRemarksKey = "remarks";
    public static final String kPAPFieldWaiterKey = "waiter";
    // Also for Photo
    public static final String kPAPFieldLocalRestaurantKey = "restaurantRef";

    // For PeopleInEvent
    public static final String kPAPFieldUserKey = "userRef";
    public static final String kPAPFieldEventKey = "eventRef";

    // For Photo
    public static final String kPAPFieldOriginalImageKey = "originalFile";
    public static final String kPAPFieldThumbnailImageKey = "thumbnailFile";
    public static final String kPAPFieldOriginalUrlKey = "originalUrl";
    public static final String kPAPFieldThumbnailUrlKey = "thumbnailUrl";
    public static final String kPAPFieldUsedRefKey = "usedRef";
    public static final String kPAPFieldUsedTypeKey = "usedType";
    public static final String kPAPFieldOSTypeKey = "osType";

    // For Recipe
    public static final String kPAPFieldPriceKey = "price";
    public static final String kPAPFieldEventRefKey = "eventRef";
    public static final String kPAPFieldOrderedPeopleRefKey = "orderedPeopleRef";

    // For Restaurant
    public static final String kPAPFieldLocationKey = "location";
    // Also for Team
    public static final String kPAPFieldAddressKey = "address";
    public static final String kPAPFieldCityStateKey = "cityState";

    // For Review
    public static final String kPAPFieldContentKey = "content";
    public static final String kPAPFieldRateKey = "rate";
    public static final String kPAPFieldUserRefKey = "userRef";
    public static final String kPAPFieldReviewRefKey = "reviewRef";
    public static final String kPAPFieldReviewTypeKey = "reviewType";

    // For Team
    public static final String kPAPFieldEmailKey = "email";

        // MARK: Anonymous User
    public static DBTeam getAnonymousUser() {
        DBTeam team = new DBTeam();
        team.setDisplayName("Anonymous");

        return team;
    }

    public static final int SOURCE_NEARBY_RESTAURANTS = 11;
    public static final int SOURCE_RESTAURANT_DETAIL = 12;
    public static final int SOURCE_EVENT_DETAIL = 13;
    public static final int SOURCE_ORDERED_RECIPES = 14;
    public static final int SOURCE_RECIPE_DETAIL = 15;

    public static final int SOURCE_RESTAURANT_EDIT = 16;
    public static final int SOURCE_EVENT_EDIT = 17;
    public static final int SOURCE_RECIPE_EDIT = 18;
    public static final int SOURCE_PEOPLE_EDIT = 19;

    public static final int SOURCE_CHOICE_PEOPLE = 20;

    public static final int limit_reviews = 4;
    public static final int limit_reviews_no = Integer.MAX_VALUE;
}
