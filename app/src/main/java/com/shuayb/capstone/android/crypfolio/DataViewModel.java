package com.shuayb.capstone.android.crypfolio;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shuayb.capstone.android.crypfolio.DataUtils.JsonUtils;
import com.shuayb.capstone.android.crypfolio.DataUtils.NetworkUtils;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;

import java.util.ArrayList;

public class DataViewModel extends ViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private MutableLiveData<ArrayList<Crypto>> cryptos;
    //private MutableLiveData<ArrayList<Crypto>> portfolioItemsMoreDetails;


    public MutableLiveData<ArrayList<Crypto>> getCryptos() {
        if (cryptos == null) {
            cryptos = new MutableLiveData<>();
            cryptos.setValue(new ArrayList<Crypto>());
        }
        return cryptos;
    }
/*
    public MutableLiveData<ArrayList<Crypto>> getPortfolioItemsMoreDetails() {
        if (portfolioItemsMoreDetails == null) {
            portfolioItemsMoreDetails = new MutableLiveData<>();
            portfolioItemsMoreDetails.setValue(new ArrayList<Crypto>());
        }
        return portfolioItemsMoreDetails;
    }
*/

/*
    public void clearPortfolioItemsMoreDetails() {
        portfolioItemsMoreDetails = new MutableLiveData<>();
        portfolioItemsMoreDetails.setValue(new ArrayList<Crypto>());
    }
*/
    public void refreshCryptos(final Context context) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForMarketviewData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "refreshCryptos: onResponse got this data: " + response);
                if (cryptos == null) {
                    cryptos = new MutableLiveData<>();
                }
                //TODO - Delete this part that randomizes the first value
                ArrayList<Crypto> temp = JsonUtils.convertJsonToCryptoList(response);
                temp.get(0).setCurrentPrice(Math.random());
                cryptos.setValue(temp);
                //cryptos.setValue(JsonUtils.convertJsonToCryptoList(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error refreshing data in refreshCryptos!!  " + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }
/*
    public void refreshPortfolioItemsMoreDetails(final Context context, String ids) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForPortfolioData(ids.toString()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "refreshPortfolio: onResponse got this data: " + response);
                if (portfolioItemsMoreDetails == null) {
                    portfolioItemsMoreDetails = new MutableLiveData<>();
                }
                ArrayList<Crypto> temp = JsonUtils.convertJsonToCryptoList(response);
                //TODO - Delete this part that randomizes the first value
                if (temp.size() > 0) {
                    temp.get(0).setCurrentPrice(Math.random());
                }
                portfolioItemsMoreDetails.setValue(temp);
                //portfolioItemsMoreDetails.setValue(JsonUtils.convertJsonToCryptoList(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error in refreshPortfolioItemsMoreDetails!!!!!!!! " + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }
*/
    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared() method called");
    }
}
