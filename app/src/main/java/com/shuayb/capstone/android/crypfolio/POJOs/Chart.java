package com.shuayb.capstone.android.crypfolio.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.shuayb.capstone.android.crypfolio.DataUtils.JsonUtils;

import java.util.ArrayList;

public class Chart implements Parcelable {
    private ArrayList times;  //time in seconds ago from present (ie. 10 would be 10 seconds ago)
    private ArrayList prices;

    public Chart(ArrayList times, ArrayList prices) {
        this.times = times;
        this.prices = prices;
    }

    public Chart (Parcel parcel) {
        this.times = parcel.readArrayList(null);
        this.prices = parcel.readArrayList(null);
    }

    public ArrayList getTimes() {
        return times;
    }

    public void setTimes(ArrayList times) {
        this.times = times;
    }

    public ArrayList getPrices() {
        return prices;
    }

    public void setPrices(ArrayList prices) {
        this.prices = prices;
    }


    public static Creator<Chart> CREATOR = new Creator<Chart>() {

        @Override
        public Chart createFromParcel(Parcel source) {
            return new Chart(source);
        }

        @Override
        public Chart[] newArray(int size) {
            return new Chart[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(times);
        dest.writeList(prices);
    }
}
