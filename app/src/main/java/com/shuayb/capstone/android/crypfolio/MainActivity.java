package com.shuayb.capstone.android.crypfolio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.shuayb.capstone.android.crypfolio.CustomAdapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DataUtils.JsonUtils;
import com.shuayb.capstone.android.crypfolio.DataUtils.NetworkUtils;
import com.shuayb.capstone.android.crypfolio.Fragments.MarketviewFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.PortfolioFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.WatchlistFragment;
import com.shuayb.capstone.android.crypfolio.POJOs.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements MarketRecyclerViewAdapter.MarketItemClickListener {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding mBinding;
    private TabLayout mTabLayout;

    ArrayList<Crypto> cryptos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //On initial startup, fetch the data online first, then display the fragments and tabs
        fetchJsonData();
    }


    //Helper method to start fetching data from Coingecko
    private void fetchJsonData() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForMarketviewData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse got this data: " + response);
                cryptos = JsonUtils.convertJsonToCryptoList(response);

                Fragment fragment = new MarketviewFragment(cryptos);
                setFragment(fragment);

                setupMainTabs();
                setupTopTabs();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error!!!!!!!! " + error.getMessage());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    //Method to set up the behaviour of the tabs
    //The tabs determine which fragment will be displayed
    //Except for settings, which launches a new activity
    private void setupMainTabs() {
        mBinding.tabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;

                switch (mBinding.tabLayoutMain.getSelectedTabPosition()) {
                    case 0:     //Markets tab
                        mBinding.tabLayoutTop.setVisibility(View.VISIBLE);
                        if (mBinding.tabLayoutTop.getSelectedTabPosition() == 0) { //Load marketview fragment
                            fragment = new MarketviewFragment(cryptos);
                        } else { //Load watchlist fragment
                            fragment = new WatchlistFragment();
                        }
                        setFragment(fragment);
                        break;
                    case 1:     //Portfolio tab
                        mBinding.tabLayoutTop.setVisibility(View.GONE);
                        fragment = new PortfolioFragment();
                        setFragment(fragment);
                        break;
                    case 2:     //Settings tab
                        Toast.makeText(getApplicationContext(), "Selected Settings tab!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Nothing to do here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Nothing to do here
            }
        });
    }


    private void setupTopTabs() {
        mBinding.tabLayoutTop.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;
                switch (mBinding.tabLayoutTop.getSelectedTabPosition()) {
                    case 0:
                        fragment = new MarketviewFragment(cryptos);
                        setFragment(fragment);
                        break;
                    case 1:
                        fragment = new WatchlistFragment();
                        setFragment(fragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Nothing to do here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Nothing to do here
            }
        });
    }

    //Helper method to set the displayed fragment in the main activity
    private void setFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentById(R.id.frag_main) != null) {
            fm.beginTransaction()
                    .replace(R.id.frag_main, fragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .add(R.id.frag_main, fragment)
                    .commit();
        }
    }

    @Override
    public void onMarketItemClick(Crypto crypto) {
        Toast.makeText(this, "You clicked on " + crypto.getName(), Toast.LENGTH_SHORT).show();
    }
}
