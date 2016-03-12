package org.wikipedia.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.text.format.DateUtils;
import android.util.SparseArray;
import android.view.View;

import org.ieatta.R;
import org.json.JSONException;
import org.json.JSONObject;
import org.ieatta.IEATTAApp;
import org.wikipedia.page.PageTitle;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * A collection of localization related methods.
 *
 * Note the distinction between Article language and device language.
 * Article language is the language of the current page content.
 * Device language is the current language setting in the device system settings.
 * Those can be different.
 */
public final class L10nUtil {
    /**
     * List of wiki language codes for which the content is primarily RTL.
     *
     * Ensure that this is always sorted alphabetically.
     */
    private static final String[] RTL_LANGS = {
            "ar", "arc", "arz", "bcc", "bqi", "ckb", "dv", "fa", "glk", "he",
            "khw", "ks", "mzn", "pnb", "ps", "sd", "ug", "ur", "yi"
    };

    /**
     * Returns true if the given wiki language is to be displayed RTL.
     *
     * @param lang Wiki code for the language to check for directionality
     * @return true if it is RTL, false if LTR
     */
    public static boolean isLangRTL(String lang) {
        return Arrays.binarySearch(RTL_LANGS, lang, null) >= 0;
    }



    /**
     * Sets text direction (RTL / LTR) for given view based on given lang.
     *
     * Doesn't do anything on pre Android 4.2, since their RTL support is terrible.
     *
     * @param view View to set text direction of
     * @param lang Wiki code for the language based on which to set direction
     */
    public static void setConditionalTextDirection(View view, String lang) {
        if (ApiUtil.hasJellyBeanMr1()) {
            view.setTextDirection(isLangRTL(lang) ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);
        }
    }

    /**
     * Sets layout direction (RTL / LTR) for given view based on given lang.
     *
     * Doesn't do anything on pre Android 4.2, since their RTL support is terrible.
     *
     * @param view View to set layout direction of
     * @param lang Wiki code for the language based on which to set direction
     */
    public static void setConditionalLayoutDirection(View view, String lang) {
        if (ApiUtil.hasJellyBeanMr1()) {
            view.setLayoutDirection(isLangRTL(lang) ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }


    /**
     * Returns true if the device languages is set to an RTL language. Note that this includes
     * RTL_Arabic (AL).
     *
     * @return true if RTL, false if not RTL
     */
    public static boolean isDeviceRTL() {
        return isCharRTL(Locale.getDefault().getDisplayName().charAt(0));
    }

    public static boolean isCharRTL(char c) {
        final int dir = Character.getDirectionality(c);
        return dir == Character.DIRECTIONALITY_RIGHT_TO_LEFT
                || dir == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public static String getStringForArticleLanguage(PageTitle title, int resId) {
        return getStringsForLocale(new Locale(title.getSite().getLanguageCode()), new int[]{resId}).get(resId);
    }

    public static SparseArray<String> getStringsForArticleLanguage(PageTitle title, int[] resId) {
        return getStringsForLocale(new Locale(title.getSite().getLanguageCode()), resId);
    }

    /**
     * Get a string resource associated with a specific target locale.  This requires working around
     * Android's internal localization logic; as such, it isn't pretty.
     *
     * See http://stackoverflow.com/a/6380008 (submitted by WMF's own Anomie!).
     */
    private static SparseArray<String> getStringsForLocale(Locale targetLocale, @StringRes int[] strings) {
        Configuration config = getCurrentConfiguration();
        Locale systemLocale = config.locale;
        config.locale = targetLocale;
        SparseArray<String> localizedStrings = getTargetStrings(strings, config);
        config.locale = systemLocale;
        resetConfiguration(config);
        return localizedStrings;
    }

    private static Configuration getCurrentConfiguration() {
        return new Configuration(IEATTAApp.getInstance().getResources().getConfiguration());
    }

    private static SparseArray<String> getTargetStrings(@StringRes int[] strings, Configuration altConfig) {
        SparseArray<String> localizedStrings = new SparseArray<>();
        Resources targetResources = new Resources(IEATTAApp.getInstance().getResources().getAssets(),
                                                  IEATTAApp.getInstance().getResources().getDisplayMetrics(),
                                                  altConfig);
        for (int stringRes : strings) {
            localizedStrings.put(stringRes, targetResources.getString(stringRes));
        }
        return localizedStrings;
    }

    /**
     * Reset the system resources by initializing a new Resources object with the original configuration.
     * @param defaultConfig The original system configuration
     */
    private static void resetConfiguration(Configuration defaultConfig) {
        new Resources(IEATTAApp.getInstance().getResources().getAssets(),
                      IEATTAApp.getInstance().getResources().getDisplayMetrics(),
                      defaultConfig);
    }

    /**
     * Formats provided date relative to the current system time
     * @param date Date to format
     * @return String representing the relative time difference of the paramter from current time
     */
    public static String formatDateRelative(Date date) {
        return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, 0).toString();
    }

    private L10nUtil() {
    }
}
