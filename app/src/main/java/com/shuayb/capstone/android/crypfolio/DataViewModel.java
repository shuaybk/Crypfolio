package com.shuayb.capstone.android.crypfolio;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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


    public MutableLiveData<ArrayList<Crypto>> getCryptos() {
        if (cryptos == null) {
            cryptos = new MutableLiveData<>();
        }
        return cryptos;
    }

    public void refreshCryptos(final Context context) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForMarketviewData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse got this data: " + response);
                if (cryptos == null) {
                    cryptos = new MutableLiveData<>();
                }
                ArrayList<Crypto> temp = JsonUtils.convertJsonToCryptoList(response);
                temp.get(0).setCurrentPrice(Math.random());
                cryptos.setValue(temp);
                //cryptos.setValue(JsonUtils.convertJsonToCryptoList(response));
                Toast.makeText(context, "The data is updated in ViewModel", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error refreshing data!!  " + error.getMessage());
                if (cryptos == null) {  //Set empty list if we don't have old info already stored
                    cryptos = new MutableLiveData<>();
                    cryptos.setValue(new ArrayList<Crypto>());
                }
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared() method called");
    }
}
