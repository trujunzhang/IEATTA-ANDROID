package org.ieatta.cells.model;

import android.support.annotation.NonNull;

import com.tableview.utils.CollectionUtil;

import org.ieatta.R;
import org.ieatta.provide.MainSegueIdentifier;

import java.util.List;

public class IEANearRestaurantMore {
    public int iconResId;
    public int titleResId;
    public MainSegueIdentifier identifier = MainSegueIdentifier.Unspecified;

    public IEANearRestaurantMore(int iconResId, int titleResId, MainSegueIdentifier identifier) {
        this.iconResId = iconResId;
        this.titleResId = titleResId;
        this.identifier = identifier;
    }

//    @NonNull
//    public static List<IEANearRestaurantMore> getMoresItems() {
//        IEANearRestaurantMore managerRestaurantItem = new IEANearRestaurantMore(R.drawable.restaurants_icon, R.string.Add_a_Restaurant, MainSegueIdentifier.editRestaurantSegueIdentifier);
//        IEANearRestaurantMore searchRestaurant = new IEANearRestaurantMore(R.drawable.nav_search, R.string.Search_Restaurants, MainSegueIdentifier.searchRestaurantSegueIdentifier);
//        IEANearRestaurantMore managerPeople = new IEANearRestaurantMore(R.drawable.nav_add_friends, R.string.Manage_Friends, MainSegueIdentifier.managerPeopleSegueIdentifier);
//        IEANearRestaurantMore readReviews = new IEANearRestaurantMore(R.drawable.nav_passport_reviews, R.string.Read_Reviews, MainSegueIdentifier.readReviewsSegueIdentifier);

//        return CollectionUtil.createList(new IEANearRestaurantMore[]{managerRestaurantItem, searchRestaurant, managerPeople, readReviews});
//    }
}
