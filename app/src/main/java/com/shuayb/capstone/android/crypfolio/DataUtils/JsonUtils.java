package com.shuayb.capstone.android.crypfolio.DataUtils;

import android.provider.ContactsContract;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.GsonBuilder;
import com.shuayb.capstone.android.crypfolio.CustomAdapters.GsonCustomDeserializer;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.POJOs.Chart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JsonUtils {

    private static final String TAG = "JsonUtils";
    private static final String PRICES_KEY = "prices";
    private static final int TIME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");


    //Creates the ArrayList of Crypto data using Gson library and some manual parsing
    public static ArrayList<Crypto> convertJsonToCryptoList(String jsonData) {
        ArrayList<Crypto> cryptos = new ArrayList<Crypto>();

        if (jsonData != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonData);

                //Give Gson our custom adapter so it only pulls the information we want
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Crypto.class, new GsonCustomDeserializer());

                for (int i = 0; i < jsonArray.length(); i++) {
                    Crypto crypto = gsonBuilder.create().fromJson(jsonArray.getJSONObject(i).toString(), Crypto.class);
                    cryptos.add(crypto);
                }

            } catch (JSONException e) {
                Log.e(TAG, "JSON Exception Error!!!!!!!  " + e.getMessage());
                //No half measures!
                cryptos.clear();
                return cryptos;
            }
        }
        return cryptos;
    }

    public static Chart convertJsonToChart(String jsonData) {
        ArrayList times = new ArrayList();
        ArrayList prices = new ArrayList();

        if (jsonData != null) {
            try {
                JSONObject json = new JSONObject(jsonData);
                JSONArray pricesJson = json.getJSONArray(PRICES_KEY);
                for (int i = 0; i < pricesJson.length(); i++) {
                    long time = pricesJson.getJSONArray(i).getLong(TIME_INDEX);
                    double price = pricesJson.getJSONArray(i).getDouble(PRICE_INDEX);

                    Date date = new Date(time);

                    times.add(formatter.format(date));
                    prices.add(new BarEntry((float)price, i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new Chart(times, prices);
    }


}
