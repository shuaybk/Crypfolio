package com.shuayb.capstone.android.crypfolio.POJOs;

public class Crypto {

    //If any double values are set to -1, that means N/A (determined during parsing)

    private String id; //Use this to do lookups on the API
    private String name;
    private String symbol;
    private String image;
    private double currentPrice;
    private double marketCap;
    private double high24h;
    private double low24h;
    private double priceChangePercent24h;
    private double circSupply;
    private double totalSupply;
    private double ath;
    private double athChangePercent;
    private String athDate;
    private String lastUpdated;

    public Crypto(String id, String name, String symbol, String image, double currentPrice,
                  double marketCap, double high24h, double low24h, double priceChangePercent24h,
                  double circSupply, double totalSupply, double ath, double athChangePercent,
                  String athDate, String lastUpdated) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.image = image;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
        this.high24h = high24h;
        this.low24h = low24h;
        this.priceChangePercent24h = priceChangePercent24h;
        this.circSupply = circSupply;
        this.totalSupply = totalSupply;
        this.ath = ath;
        this.athChangePercent = athChangePercent;
        this.athDate = athDate;
        this.lastUpdated = lastUpdated;
    }

    /////////////////////////////////
    //////////// Getters ////////////
    /////////////////////////////////

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getImage() {
        return image;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getHigh24h() {
        return high24h;
    }

    public double getLow24h() {
        return low24h;
    }

    public double getPriceChangePercent24h() {
        return priceChangePercent24h;
    }

    public double getCircSupply() {
        return circSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public double getAth() {
        return ath;
    }

    public double getAthChangePercent() {
        return athChangePercent;
    }

    public String getAthDate() {
        return athDate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }


    /////////////////////////////////
    //////////// Setters ////////////
    /////////////////////////////////

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public void setHigh24h(double high24h) {
        this.high24h = high24h;
    }

    public void setLow24h(double low24h) {
        this.low24h = low24h;
    }

    public void setPriceChangePercent24h(double priceChangePercent24h) {
        this.priceChangePercent24h = priceChangePercent24h;
    }

    public void setCircSupply(double circSupply) {
        this.circSupply = circSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public void setAth(double ath) {
        this.ath = ath;
    }

    public void setAthChangePercent(double athChangePercent) {
        this.athChangePercent = athChangePercent;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
