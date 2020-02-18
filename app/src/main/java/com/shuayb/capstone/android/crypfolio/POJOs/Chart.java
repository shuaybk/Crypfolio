package com.shuayb.capstone.android.crypfolio.POJOs;

import com.shuayb.capstone.android.crypfolio.DataUtils.JsonUtils;

import java.util.ArrayList;

public class Chart {
    private ArrayList times;  //time in seconds ago from present (ie. 10 would be 10 seconds ago)
    private ArrayList prices;

    public Chart(ArrayList times, ArrayList prices) {
        this.times = times;
        this.prices = prices;
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
}
