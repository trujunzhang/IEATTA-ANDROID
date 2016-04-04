package org.ieatta.location;

import android.location.Location;
import android.widget.TextView;

import org.ieatta.IEAApp;

import rx.functions.Action1;

public class UpdateLocationAction implements Action1<Location> {
    @Override
    public void call(Location location) {
        IEAApp.getInstance().lastLocation = location;
    }
}
