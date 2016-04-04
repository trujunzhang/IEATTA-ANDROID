package org.ieatta.location;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LocationActivity extends AppCompatActivity {

    private Context context;
    private TextView updatableLocationView;

    private LocationHandler locationHandler;

    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showLastLocation() {
        locationHandler.showLastLocation();
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void showUpdateLocation() {
        locationHandler.showUpdateLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.location);
//        context = getApplicationContext();
//
//        this.updatableLocationView = (TextView) findViewById(R.id.textView2);
//
//        locationHandler = new LocationHandler(context, updatableLocationView, this);
//
////        LocationActivityPermissionsDispatcher.showLastLocationWithCheck(this);
//        LocationActivityPermissionsDispatcher.showUpdateLocationWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        LocationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    protected void onStop() {
        super.onStop();
        locationHandler.onStop();
    }
}
