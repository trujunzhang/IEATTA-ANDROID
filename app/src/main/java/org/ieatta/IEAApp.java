package org.ieatta;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.maps.MapView;
//import com.squareup.otto.Bus;

import org.ieatta.activity.search.RecentSearch;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.parse.ParseAPI;
import org.ieatta.server.cache.CacheImageUtil;
import org.ieatta.server.recurring.SyncRecurringTask;
import org.wikipedia.ViewAnimations;
import org.wikipedia.crash.CrashReporter;
import org.wikipedia.crash.hockeyapp.HockeyAppCrashReporter;
import org.wikipedia.database.Database;
import org.wikipedia.database.DatabaseClient;
import org.wikipedia.drawable.DrawableUtil;
import org.wikipedia.settings.Prefs;
import org.wikipedia.theme.Theme;
import org.wikipedia.util.ReleaseUtil;
import org.wikipedia.util.log.L;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

public class IEAApp extends Application {
    private static final String HTTPS_PROTOCOL = "https";
    private static final int EVENT_LOG_TESTING_ID = new Random().nextInt(Integer.MAX_VALUE);

    private final Map<Class<?>, DatabaseClient<?>> databaseClients = Collections.synchronizedMap(new HashMap<Class<?>, DatabaseClient<?>>());
    private Database database;

    private float screenDensity;
    public Location lastLocation;

    public float getScreenDensity() {
        return screenDensity;
    }

    // Reload in onCreate to override
    public String getNetworkProtocol() {
        return HTTPS_PROTOCOL;
    }

    public static final int FONT_SIZE_MULTIPLIER_MIN = -5;
    public static final int FONT_SIZE_MULTIPLIER_MAX = 8;
    private static final float FONT_SIZE_FACTOR = 0.1f;

    public static final int PREFERRED_THUMB_SIZE = 320;

    private CrashReporter crashReporter;

    public boolean isProdRelease() {
        return ReleaseUtil.isProdRelease();
    }

    public boolean isPreProdRelease() {
        return ReleaseUtil.isPreProdRelease();
    }

    public boolean isAlphaRelease() {
        return ReleaseUtil.isAlphaRelease();
    }

    public boolean isPreBetaRelease() {
        return ReleaseUtil.isPreBetaRelease();
    }

    public boolean isDevRelease() {
        return ReleaseUtil.isDevRelease();
    }

    /**
     * Singleton instance of IEAApp
     */
    private static IEAApp INSTANCE;

    //    private Bus bus;
    @NonNull
    private Theme currentTheme = Theme.getFallback();

    public IEAApp() {
        INSTANCE = this;
    }

    /**
     * Returns the singleton instance of the IEAApp
     * <p/>
     * This is ok, since android treats it as a singleton anyway.
     */
    public static IEAApp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /// Clean up all cache folder contains downloaded original images.
        CacheImageUtil.sharedInstance.clearCacheDisk();

        ParseAPI.setup(this);
//        initExceptionHandling();

        Fresco.initialize(this);
//        bus = new Bus();

        final Resources resources = getResources();
        ViewAnimations.init(resources);
        screenDensity = resources.getDisplayMetrics().density;

        database = new Database(this);

        new SyncRecurringTask().prepareTimer();
    }

//    public Bus getBus() {
//        return bus;
//    }

    private MapView headerMapView;

    public Database getDatabase() {
        return database;
    }

    /**
     * Get this app's unique install ID, which is a UUID that should be unique for each install
     * of the app. Useful for anonymous analytics.
     *
     * @return Unique install ID for this app.
     */
    public String getAppInstallID() {
        String id = Prefs.getAppInstallId();
        if (id == null) {
            id = UUID.randomUUID().toString();
            Prefs.setAppInstallId(id);
        }
        return id;
    }

    /**
     * Get an integer-valued random ID. This is typically used to determine global EventLogging
     * sampling, that is, whether the user's instance of the app sends any events or not. This is a
     * pure technical measure which is necessary to prevent overloading EventLogging with too many
     * events. This value will persist for the lifetime of the app.
     * <p/>
     * Don't use this method when running to determine whether or not the user falls into a control
     * or test group in any kind of tests (such as A/B tests), as that would introduce sampling
     * biases which would invalidate the test.
     *
     * @return Integer ID for event log sampling.
     */
    @IntRange(from = 0)
    public int getEventLogSamplingID() {
        return EVENT_LOG_TESTING_ID;
    }

    public boolean isFeatureReadMoreSearchOpeningTextEnabled() {
        boolean enabled;
        if (Prefs.hasFeatureReadMoreSearchOpeningText()) {
            enabled = Prefs.isFeatureReadMoreSearchOpeningTextEnabled();
        } else {
            enabled = new Random().nextInt(2) == 0;
            Prefs.setFeatureReadMoreSearchOpeningTextEnabled(enabled);
        }
        return enabled;
    }

    /**
     * Gets the currently-selected theme for the app.
     *
     * @return Theme that is currently selected, which is the actual theme ID that can
     * be passed to setTheme() when creating an activity.
     */
    @NonNull
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public boolean isCurrentThemeLight() {
        return getCurrentTheme().isLight();
    }

    public boolean isCurrentThemeDark() {
        return getCurrentTheme().isDark();
    }

    @ColorInt
    public int getContrastingThemeColor() {
        return getCurrentTheme().getContrastingColor();
    }

    /**
     * Apply a tint to the provided drawable.
     *
     * @param d         Drawable to be tinted.
     * @param tintColor Color of the tint. Setting to 0 will remove the tint.
     */
    public void setDrawableTint(Drawable d, int tintColor) {
        if (tintColor == 0) {
            d.clearColorFilter();
        } else {
            DrawableUtil.setTint(d, tintColor);
        }
    }

    /**
     * Make adjustments to a Drawable object to look better in the current theme.
     * (e.g. apply a white color filter for night mode)
     *
     * @param d Drawable to be adjusted.
     */
    public void adjustDrawableToTheme(Drawable d) {
        setDrawableTint(d, isCurrentThemeDark() ? Color.WHITE : Color.TRANSPARENT);
    }

    public void putCrashReportProperty(String key, String value) {
        crashReporter.putReportProperty(key, value);
    }

    public void checkCrashes(@NonNull Activity activity) {
        crashReporter.checkCrashes(activity);
    }

    /**
     * Gets whether EventLogging is currently enabled or disabled.
     *
     * @return A boolean that is true if EventLogging is enabled, and false if it is not.
     */
    public boolean isEventLoggingEnabled() {
        return Prefs.isEventLoggingEnabled();
    }

    public boolean isImageDownloadEnabled() {
//        return Prefs.isImageDownloadEnabled();
        return true;
    }

    public boolean isLinkPreviewEnabled() {
        return Prefs.isLinkPreviewEnabled();
    }

    public SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    private void initExceptionHandling() {
        crashReporter = new HockeyAppCrashReporter(getString(R.string.hockeyapp_app_id), consentAccessor());
        crashReporter.registerCrashHandler(this);

        L.setRemoteLogger(crashReporter);
    }

    private CrashReporter.AutoUploadConsentAccessor consentAccessor() {
        return new CrashReporter.AutoUploadConsentAccessor() {
            @Override
            public boolean isAutoUploadPermitted() {
                return Prefs.isCrashReportAutoUploadEnabled();
            }
        };
    }

    public <T> DatabaseClient<T> getDatabaseClient(Class<T> cls) {
        if (!databaseClients.containsKey(cls)) {
            DatabaseClient client;
            if (cls.equals(HistoryEntry.class)) {
                client = new DatabaseClient<>(this, HistoryEntry.DATABASE_TABLE);
            } else if (cls.equals(RecentSearch.class)) {
                client = new DatabaseClient<>(this, RecentSearch.DATABASE_TABLE);
            } else {
                throw new RuntimeException("No persister found for class " + cls.getCanonicalName());
            }
            databaseClients.put(cls, client);
        }
        //noinspection unchecked
        return (DatabaseClient<T>) databaseClients.get(cls);
    }

    public MapView getHeaderMapView() {
        return headerMapView;
    }

    public void setHeaderMapView(MapView headerMapView) {
        this.headerMapView = headerMapView;
    }
}
