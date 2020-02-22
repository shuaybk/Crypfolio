package com.shuayb.capstone.android.crypfolio;

import android.content.Context;
import android.util.Log;

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
    private ArrayList<Crypto> cryptos;
    private OnDataRefreshedListener mCallback;

    public interface OnDataRefreshedListener {
        public void onDataRefresh();
    }


    public ArrayList<Crypto> getCryptos() {
        return cryptos;
    }

    public void refreshCryptos(Context context) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForMarketviewData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse got this data: " + response);
                cryptos = JsonUtils.convertJsonToCryptoList(response);
                if (mCallback != null) {
                    mCallback.onDataRefresh();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error refreshing data!!  " + error.getMessage());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    public void setCallback(OnDataRefreshedListener listener) {
        mCallback = listener;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared() method called");
    }
}
