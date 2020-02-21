package com.shuayb.capstone.android.crypfolio.POJOs;

public class PortfolioItem {

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
}
