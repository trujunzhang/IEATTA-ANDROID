package org.wikipedia.analytics;

import android.support.annotation.NonNull;

import org.ieatta.IEAApp;
import org.wikipedia.Site;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

/*package*/public abstract class TimedFunnel extends Funnel {
    private long startTime;

    /*package*/public TimedFunnel(IEAApp app, String schemaName, int revision, int sampleRate) {
        this(app, schemaName, revision, sampleRate, null);
    }

    /*package*/public TimedFunnel(IEAApp app, String schemaName, int revision, int sampleRate, Site site) {
        super(app, schemaName, revision, sampleRate, site);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected JSONObject preprocessData(@NonNull JSONObject eventData) {
        preprocessData(eventData, getDurationFieldName(), getDurationSeconds());
        return super.preprocessData(eventData);
    }

    /** Override me for deviant implementations. */
    protected String getDurationFieldName() {
        return "timeSpent";
    }

    protected void resetDuration() {
        startTime = System.currentTimeMillis();
    }

    private long getDuration() {
        return System.currentTimeMillis() - startTime;
    }

    private long getDurationSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(getDuration());
    }
}
