package com.shuayb.capstone.android.crypfolio.DatabaseUtils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.shuayb.capstone.android.crypfolio.DataUtils.RandomUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity(tableName = "watchlist")
public class Crypto implements Parcelable {

    //If any double values are set to -1, that means N/A (determined during parsing)

    @PrimaryKey
    @NonNull
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

    @Ignore
    public Crypto (Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.symbol = parcel.readString();
        this.image = parcel.readString();
        this.currentPrice = parcel.readDouble();
        this.marketCap = parcel.readDouble();
        this.high24h = parcel.readDouble();
        this.low24h = parcel.readDouble();
        this.priceChangePercent24h = parcel.readDouble();
        this.circSupply = parcel.readDouble();
        this.totalSupply = parcel.readDouble();
        this.ath = parcel.readDouble();
        this.athChangePercent = parcel.readDouble();
        this.athDate = parcel.readString();
        this.lastUpdated = parcel.readString();
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


    /////////////////////////////////
    ///////// More methods //////////
    /////////////////////////////////

    @Ignore
    public String getFormattedMarketcapFull() {
        StringBuilder str = new StringBuilder("$" + new BigDecimal(marketCap).toPlainString());

        if (str.length() < 6) {
            return str.toString();
        } else {
            //Add a comma every 3 characters starting from the right
            int counter = 0;
            for (int i = str.length()-1; i > 1; i--) {
                if (counter == 2) {
                    str.insert(i, ",");
                }
                counter = (counter + 1) % 3;
            }
        }
        return str.toString();
    }

    @Ignore
    public String getFormattedMarketcapShort() {
        StringBuilder str = new StringBuilder("$");

        if (marketCap < 1000000) {
            return getFormattedMarketcapFull();
        } else {
            if (marketCap < 1000000000) {  //For the millions range
                str.append((int) (marketCap / 1000000));
                str.append(".");
                int decimalPortion = (int) (marketCap / 10000) - ((int) (marketCap / 1000000) * 100);
                str.append(decimalPortion);
                str.append("M");
            } else if (marketCap < 1000000000000f) {
                str.append((int) (marketCap / 1000000000));
                str.append(".");
                int decimalPortion = (int) (marketCap / 10000000) - ((int) (marketCap / 1000000000) * 100);
                str.append(decimalPortion);
                str.append("B");
            } else {
                str.append((int) (marketCap / 1000000000000f));
                str.append(".");
                int decimalPortion = (int) (marketCap / 10000000000f) - ((int) (marketCap / 1000000000000f) * 100);
                str.append(decimalPortion);
                str.append("T");
            }
        }
        return str.toString();
    }


    @Ignore
    public static Creator<Crypto> CREATOR = new Creator<Crypto>() {

        @Override
        public Crypto createFromParcel(Parcel source) {
            return new Crypto(source);
        }

        @Override
        public Crypto[] newArray(int size) {
            return new Crypto[size];
        }
    };

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeString(image);
        dest.writeDouble(currentPrice);
        dest.writeDouble(marketCap);
        dest.writeDouble(high24h);
        dest.writeDouble(low24h);
        dest.writeDouble(priceChangePercent24h);
        dest.writeDouble(circSupply);
        dest.writeDouble(totalSupply);
        dest.writeDouble(ath);
        dest.writeDouble(athChangePercent);
        dest.writeString(athDate);
        dest.writeString(lastUpdated);
    }
}
