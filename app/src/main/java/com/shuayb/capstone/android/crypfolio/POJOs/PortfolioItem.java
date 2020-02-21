package com.shuayb.capstone.android.crypfolio.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

public class PortfolioItem implements Parcelable {

    private String id;
    private String name;
    private String image;
    private double amount;
    private double avgPrice;
    private double currentPrice;

    public PortfolioItem(String id, String name, String image, double amount, double avgPrice, double currentPrice) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.amount = amount;
        this.avgPrice = avgPrice;
        this.currentPrice = currentPrice;
    }

    public PortfolioItem(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.image = parcel.readString();
        this.amount = parcel.readDouble();
        this.avgPrice = parcel.readDouble();
        this.currentPrice = parcel.readDouble();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }


    public static Creator<PortfolioItem> CREATOR = new Creator<PortfolioItem>() {

        @Override
        public PortfolioItem createFromParcel(Parcel source) {
            return new PortfolioItem(source);
        }

        @Override
        public PortfolioItem[] newArray(int size) {
            return new PortfolioItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeDouble(amount);
        dest.writeDouble(avgPrice);
        dest.writeDouble(currentPrice);
    }
}
