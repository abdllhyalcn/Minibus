package com.example.minibus;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LocationData implements Parcelable {
    private double Latitude;
    private double Longitude;
    private double Speed;
    private long date;

    public Address getLocationAddress(Context context){
        List<Address> list;
        try {
            list=new Geocoder(context, Locale.getDefault()).getFromLocation(getLatitude(), getLongitude(),
                    // In this sample, get just a single address.
                    1);
            return list.get(0);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Log.e(TAG, ioException.toString());
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e(TAG, illegalArgumentException.toString());
        }
        return null;
    }

    public LocationData(){}

    public LocationData(double latitude, double longitude, double speed, long date) {
        Latitude = latitude;
        Longitude = longitude;
        Speed = speed;
        this.date = date;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getSpeed() {
        return Speed;
    }

    public void setSpeed(double speed) {
        Speed = speed;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.Latitude);
        dest.writeDouble(this.Longitude);
        dest.writeDouble(this.Speed);
        dest.writeLong(this.date);
    }

    protected LocationData(Parcel in) {
        this.Latitude = in.readDouble();
        this.Longitude = in.readDouble();
        this.Speed = in.readDouble();
        this.date = in.readLong();
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        @Override
        public LocationData createFromParcel(Parcel source) {
            return new LocationData(source);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };
}
