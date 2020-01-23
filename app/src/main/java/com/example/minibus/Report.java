package com.example.minibus;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Report implements Parcelable {
    private String startAddress,finishAddress;
    private long startDate, finishDate;

    public Report(){}

    public Report(String startAddress, String finishAddress, long startDate, long finishDate) {
        this.startAddress = startAddress;
        this.finishAddress = finishAddress;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getFinishAddress() {
        return finishAddress;
    }

    public void setFinishAddress(String finishAddress) {
        this.finishAddress = finishAddress;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(long finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startAddress);
        dest.writeString(this.finishAddress);
        dest.writeLong(this.startDate);
        dest.writeLong(this.finishDate);
    }

    protected Report(Parcel in) {
        this.startAddress = in.readString();
        this.finishAddress = in.readString();
        this.startDate = in.readLong();
        this.finishDate = in.readLong();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
