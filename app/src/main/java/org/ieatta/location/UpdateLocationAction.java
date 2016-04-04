package org.ieatta.location;

import android.location.Location;
import android.widget.TextView;

import org.ieatta.IEAApp;
import org.ieatta.activity.PageActivity;
import org.ieatta.utils.LocationUtil;

import rx.functions.Action1;

public class UpdateLocationAction implements Action1<Location> {
    private final PageActivity activity;

    public UpdateLocationAction(PageActivity activity) {
        this.activity = activity;
    }

    @Override
    public void call(Location location) {
//        IEAApp.getInstance().lastLocation = location;
        IEAApp.getInstance().lastLocation = LocationUtil.getLocation();
        this.activity.updateLocation();
    }
}
