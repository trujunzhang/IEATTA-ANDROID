package org.ieatta.activity.leadimages;


import android.view.View;

import org.ieatta.R;
import org.ieatta.parse.AppConstant;

public final class MenuBarItemVisibilities {

    public static int[] menuBarItemsVisibility(int source) {
        switch (source) {
            case AppConstant.SOURCE_RESTAURANT_DETAIL:
                return new int[]{View.VISIBLE};
            case AppConstant.SOURCE_EVENT_DETAIL:
                break;
            case AppConstant.SOURCE_ORDERED_RECIPES:
                break;
            case AppConstant.SOURCE_RECIPE_DETAIL:
                break;
        }
        return null;
    }

//            R.id.view_article_menu_bar_bookmark,
//            R.id.view_article_menu_bar_share,
//            R.id.view_article_menu_bar_navigate,
//
//            R.id.view_article_menu_bar_write_review,
//            R.id.view_article_menu_bar_see_reviews,
//            R.id.view_article_menu_bar_edit,
//
//            R.id.view_article_menu_bar_add_event,
//            R.id.view_article_menu_bar_select_person,
//            R.id.view_article_menu_bar_add_food,

}
