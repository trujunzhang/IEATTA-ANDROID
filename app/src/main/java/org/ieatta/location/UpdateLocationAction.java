package org.ieatta.location;

import android.location.Location;
import android.widget.TextView;

import org.ieatta.IEAApp;
import org.ieatta.activity.PageActivity;

import rx.functions.Action1;

public class UpdateLocationAction implements Action1<Location> {
    private final PageActivity activity;

    public UpdateLocationAction(PageActivity activity) {
        this.activity = activity;
    }

    @Override
    public void call(Location location) {
        IEAApp.getInstance().lastLocation = location;
        this.activity.updateLocation();
    }
}
