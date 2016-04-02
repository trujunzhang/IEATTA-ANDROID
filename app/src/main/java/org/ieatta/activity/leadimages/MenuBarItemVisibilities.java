package org.ieatta.activity.leadimages;


import android.view.View;

import org.ieatta.R;
import org.ieatta.parse.AppConstant;

import java.util.HashMap;
import java.util.LinkedHashMap;

public final class MenuBarItemVisibilities {

    public static final int type_menu_bar_bookmark = 0;
    public static final int type_menu_bar_share = 1;
    public static final int type_menu_bar_navigate = 2;

    public static final int type_menu_bar_write_review = 3;
    public static final int type_menu_bar_see_reviews = 4;
    public static final int type_menu_bar_edit = 5;

    public static final int type_menu_bar_add_event = 6;
    public static final int type_menu_bar_select_person = 7;
    public static final int type_menu_bar_add_food = 8;

    public static HashMap<Integer, Integer> menuBarItemsVisibility(int source) {
        switch (source) {
            case AppConstant.SOURCE_RESTAURANT_DETAIL:
                return new LinkedHashMap<Integer,Integer>() {{
                    put(type_menu_bar_bookmark, View.GONE);
                    put(type_menu_bar_share, View.GONE);
                    put(type_menu_bar_navigate, View.GONE);

                    put(type_menu_bar_write_review, View.GONE);
                    put(type_menu_bar_see_reviews, View.GONE);
                    put(type_menu_bar_edit, View.GONE);

                    put(type_menu_bar_add_event, View.GONE);
                    put(type_menu_bar_select_person, View.GONE);
                    put(type_menu_bar_add_food, View.GONE);
                }};
            case AppConstant.SOURCE_EVENT_DETAIL:
                return new LinkedHashMap<Integer,Integer>() {{
                    put(type_menu_bar_bookmark, View.GONE);
                    put(type_menu_bar_share, View.GONE);
                    put(type_menu_bar_navigate, View.GONE);

                    put(type_menu_bar_write_review, View.GONE);
                    put(type_menu_bar_see_reviews, View.GONE);
                    put(type_menu_bar_edit, View.GONE);

                    put(type_menu_bar_add_event, View.GONE);
                    put(type_menu_bar_select_person, View.GONE);
                    put(type_menu_bar_add_food, View.GONE);
                }};
            case AppConstant.SOURCE_ORDERED_RECIPES:
                return new LinkedHashMap<Integer,Integer>() {{
                    put(type_menu_bar_bookmark, View.GONE);
                    put(type_menu_bar_share, View.GONE);
                    put(type_menu_bar_navigate, View.GONE);

                    put(type_menu_bar_write_review, View.GONE);
                    put(type_menu_bar_see_reviews, View.GONE);
                    put(type_menu_bar_edit, View.GONE);

                    put(type_menu_bar_add_event, View.GONE);
                    put(type_menu_bar_select_person, View.GONE);
                    put(type_menu_bar_add_food, View.GONE);
                }};
            case AppConstant.SOURCE_RECIPE_DETAIL:
                return new LinkedHashMap<Integer,Integer>() {{
                    put(type_menu_bar_bookmark, View.GONE);
                    put(type_menu_bar_share, View.GONE);
                    put(type_menu_bar_navigate, View.GONE);

                    put(type_menu_bar_write_review, View.GONE);
                    put(type_menu_bar_see_reviews, View.GONE);
                    put(type_menu_bar_edit, View.GONE);

                    put(type_menu_bar_add_event, View.GONE);
                    put(type_menu_bar_select_person, View.GONE);
                    put(type_menu_bar_add_food, View.GONE);
                }};
        }
        return null;
    }


}
