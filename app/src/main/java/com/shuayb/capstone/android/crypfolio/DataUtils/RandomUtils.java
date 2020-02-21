package com.shuayb.capstone.android.crypfolio.DataUtils;

import java.text.DecimalFormat;

public class RandomUtils {

    public static String getFormattedCurrencyAmount(double price) {
        String result;

        if (Math.abs(price) > 5) { //To 2 decimal points
            double priceRounded = Math.round(price * 100)/100.0;
            DecimalFormat df = new DecimalFormat("0.00");
            result = df.format(priceRounded);

        } else if (Math.abs(price) > 1) { //To 3 decimals
            double priceRounded = Math.round(price * 1000)/1000.0;
            DecimalFormat df = new DecimalFormat("0.000");
            result = df.format(priceRounded);

        } else { //To 6 decimals
            double priceRounded = Math.round(price * 1000000)/1000000.0;
            DecimalFormat df = new DecimalFormat("0.000000");
            result = df.format(priceRounded);
        }

        return result;
    }

    //Round to 2 decimal places
    public static String getFormattedPercentage(double percent) {
        String result;

        double priceRounded = Math.round(percent * 100)/100.0;
        DecimalFormat df = new DecimalFormat("0.00");
        result = df.format(priceRounded) + "%";

        return result;
    }

    //Rounds a number to a reasonable number of decimal places
    public static String roundToReasonableValue(double val) {
        String result;

        if (Math.abs(val) > 5) { //Round to 2 decimal places
            double valRounded = Math.round(val * 100)/100.0;
            DecimalFormat df = new DecimalFormat("0.00");
            result = df.format(valRounded);
        } else if (Math.abs(val) > 1) { //Round to 3 decimal places
            double valRounded = Math.round(val * 1000)/1000.0;
            DecimalFormat df = new DecimalFormat("0.000");
            result = df.format(valRounded);
        } else { //Round to 6 decimal places
            double valRounded = Math.round(val * 1000000)/1000000.0;
            DecimalFormat df = new DecimalFormat("0.000000");
            result = df.format(valRounded);
        }

        return result;
    }

    public static String getNetChangeAmount(double amount, double initialPrice, double currPrice) {
        double initialValue = amount * initialPrice;
        double currValue = amount * currPrice;
        double netChange = currValue - initialValue;

        String result;

        if (netChange >= 0) {
            result = "+" + "$" + getFormattedCurrencyAmount(netChange);
        } else {
            result = "-" + "$" + getFormattedCurrencyAmount(netChange);
        }

        return result;
    }

    public static String getNetChangePercentage(double amount, double initialPrice, double currPrice) {
        double initialValue = amount * initialPrice;
        double currValue = amount * currPrice;
        double netChange = (currValue/initialValue - 1) * 100;

        String result;

        if (netChange >= 0) {
            result = "+" + getFormattedPercentage(netChange);
        } else {
            result = "-" + getFormattedPercentage(netChange);
        }

        return result;
    }

}
