package com.shuayb.capstone.android.crypfolio.DataUtils;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.shuayb.capstone.android.crypfolio.POJOs.Crypto;

import java.lang.reflect.Type;

public class GsonCustomDeserializer implements JsonDeserializer<Crypto> {

    private static final String TAG = "GsonCustomDeserializer";

    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String SYMBOL_KEY = "symbol";
    private static final String IMAGE_KEY = "image";
    private static final String CURRENT_PRICE_KEY = "current_price";
    private static final String MARKETCAP_KEY = "market_cap";
    private static final String HIGH_24H_KEY = "high_24h";
    private static final String LOW_24H_KEY = "low_24h";
    private static final String PRICE_CHANGE_PERCENT_24H_KEY = "price_change_percentage_24h";
    private static final String CIRC_SUPPLY_KEY = "circulating_supply";
    private static final String TOTAL_SUPPLY_KEY = "total_supply";
    private static final String ATH_KEY = "ath";
    private static final String ATH_CHANGE_PERCENT_KEY = "ath_change_percentage";
    private static final String ATH_DATE_KEY = "ath_date";
    private static final String LAST_UPDATED_KEY = "last_updated";

    //Defines our custom Gson deserializer so we can map the Json variables to our Crypto class
    //and ignore the fields we don't want
    @Override
    public Crypto deserialize(JsonElement jElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jObject = jElement.getAsJsonObject();

        //Set any null items to -1 if double, or "N/A" if a String

        String id = getStringValue(jObject.get(ID_KEY));

        String name = getStringValue(jObject.get(NAME_KEY));
        String symbol = getStringValue(jObject.get(SYMBOL_KEY));
        String image = getStringValue(jObject.get(IMAGE_KEY));
        double currentPrice = getDoubleValue(jObject.get(CURRENT_PRICE_KEY));
        double marketCap = getDoubleValue(jObject.get(MARKETCAP_KEY));
        double high24h = getDoubleValue(jObject.get(HIGH_24H_KEY));
        double low24h = getDoubleValue(jObject.get(LOW_24H_KEY));
        double priceChangePercent24h = getDoubleValue(jObject.get(PRICE_CHANGE_PERCENT_24H_KEY));
        double circSupply = getDoubleValue(jObject.get(CIRC_SUPPLY_KEY));
        double totalSupply = getDoubleValue(jObject.get(TOTAL_SUPPLY_KEY));
        double ath = getDoubleValue(jObject.get(ATH_KEY));
        double athChangePercent = getDoubleValue(jObject.get(ATH_CHANGE_PERCENT_KEY));
        String athDate = getStringValue(jObject.get(ATH_DATE_KEY));
        String lastUpdated = getStringValue(jObject.get(LAST_UPDATED_KEY));


        Crypto result = new Crypto(id, name, symbol, image, currentPrice, marketCap, high24h, low24h,
                priceChangePercent24h, circSupply, totalSupply, ath, athChangePercent, athDate,
                lastUpdated);

        return result;
    }

    //Helper method to get the string value from a JsonElement
    //If the element is null, set the String to "N/A"
    private String getStringValue(JsonElement jElement) {
        Log.d(TAG, "Parsing String:  " + jElement.toString());
        if (jElement.isJsonNull()) {
            return "N/A";
        }
        return jElement.getAsString();
    }

    //Helper method to get the double value from a JsonElement
    //If the element is null, set the double to -1
    private double getDoubleValue(JsonElement jElement) {
        Log.d(TAG, "Parsing double:  " + jElement.toString());
        if (jElement.isJsonNull()) {
            return -1;
        }
        return jElement.getAsDouble();
    }
}