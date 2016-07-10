package org.ieatta.activity.maps;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class LeadMapView implements Parcelable {
    private double latitude;
    private double longitude;

    private String title;
    private String description;

    public LeadMapView(double latitude, double longitude, String title, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
    }

    public LeadMapView(LatLng position, String title) {
        this.latitude = position.latitude;
        this.longitude = position.longitude;
        this.title = title;
        this.description = null;
    }

    public static final Creator<LeadMapView> CREATOR = new Creator<LeadMapView>() {
        @Override
        public LeadMapView createFromParcel(Parcel in) {
            return new LeadMapView(in);
        }

        @Override
        public LeadMapView[] newArray(int size) {
            return new LeadMapView[size];
        }
    };


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private LeadMapView(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        title = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(title);
        parcel.writeString(description);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LeadMapView)) {
            return false;
        }

        LeadMapView other = (LeadMapView) o;
        // Not using namespace directly since that can be null
        return other.getLatitude() == ((LeadMapView) o).getLatitude() && other.getLongitude() == ((LeadMapView) o).getLongitude();
    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 32 * result;
        return result;
    }

}
