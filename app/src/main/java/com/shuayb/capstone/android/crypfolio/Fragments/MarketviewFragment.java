package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shuayb.capstone.android.crypfolio.Adapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DataUtils.JsonUtils;
import com.shuayb.capstone.android.crypfolio.DataUtils.NetworkUtils;
import com.shuayb.capstone.android.crypfolio.POJOs.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.MarketviewFragmentBinding;

import java.util.ArrayList;

public class MarketviewFragment extends Fragment {
    private static final String TAG = "MarketviewFragment";

    private MarketviewFragmentBinding mBinding;

    ArrayList<Crypto> cryptos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = MarketviewFragmentBinding.inflate(inflater, container, false);

        fetchJsonData();

        return mBinding.getRoot();
    }

    private void initRecyclerView(View view) {
        MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(getContext(), cryptos);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //Helper method to start fetching data from Coingecko
    private void fetchJsonData() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForMarketviewData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse got this data: " + response);
                cryptos = JsonUtils.convertJsonToCryptoList(response);
                initRecyclerView(mBinding.getRoot());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error!!!!!!!! " + error.getMessage());
            }
        });
        mRequestQueue.add(mStringRequest);
    }


}
