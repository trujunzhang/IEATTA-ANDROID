package org.ieatta.location;


import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;


public class LocationHandler {
    private static final int REQUEST_CHECK_SETTINGS = 0;

    private final Context context;
    private final TextView updatableLocationView;
    private final LocationActivity activity;

    private ReactiveLocationProvider locationProvider;
    private Observable<Location> locationUpdatesObservable;
    private Subscription updatableLocationSubscription;

    public LocationHandler(Context context, TextView updatableLocationView,LocationActivity activity) {
        this.context = context;
        this.updatableLocationView = updatableLocationView;
        this.activity = activity;
    }

    public void showLastLocation() {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        doSthImportantWithObtainedLocation(location);
                    }
                });
    }

    private void doSthImportantWithObtainedLocation(Location location) {
        updatableLocationView.setText("longitude: " + location.getLongitude() + ",Latitude: " + location.getLatitude());
    }

    public void showUpdateLocation() {
        final LocationRequest locationRequest = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100);

        locationProvider = new ReactiveLocationProvider(context);
        locationUpdatesObservable = locationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                                .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                )
                .doOnNext(new Action1<LocationSettingsResult>() {
                    @Override
                    public void call(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException th) {
                                Log.e("MainActivity", "Error opening settings activity.", th);
                            }
                        }
                    }
                })
                .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                        return locationProvider.getUpdatedLocation(locationRequest);
                    }
                });


        updatableLocationSubscription = locationUpdatesObservable
                .map(new LocationToStringFunc())
                .map(new Func1<String, String>() {
                    int count = 0;

                    @Override
                    public String call(String s) {
                        return s + " " + count++;
                    }
                })
                .subscribe(new DisplayTextOnViewAction(updatableLocationView), new ErrorHandler());
    }


    private class ErrorHandler implements Action1<Throwable> {
        @Override
        public void call(Throwable throwable) {
//            Toast.makeText(LocationActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
            Log.d("LocationActivity", "Error occurred", throwable);
        }
    }

    public void onStop() {
        if (updatableLocationSubscription != null)
            updatableLocationSubscription.unsubscribe();
    }

}
