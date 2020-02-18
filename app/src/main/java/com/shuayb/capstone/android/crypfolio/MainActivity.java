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
import com.shuayb.capstone.android.crypfolio.Fragments.DetailsFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.MarketviewFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.PortfolioFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.WatchlistFragment;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements MarketRecyclerViewAdapter.MarketItemClickListener {

    private static final String TAG = "MainActivity";
    private static final int FRAG_MARKETVIEW = 1;
    private static final int FRAG_WATCHLIST = 2;
    private static final int FRAG_PORTFOLIO = 3;
    private static final int FRAG_DETAILS = 4;

    private ActivityMainBinding mBinding;
    private TabLayout mTabLayout;
    private int lastFragmentDisplayed;  //Keeps track of what fragment was last displayed

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

                setMarketviewFragment();

                setupBottomTabs();
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

    private void refreshData() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForMarketviewData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse got this data: " + response);
                cryptos = JsonUtils.convertJsonToCryptoList(response);

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
    private void setupBottomTabs() {
        mBinding.tabLayoutBottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (mBinding.tabLayoutBottom.getSelectedTabPosition()) {
                    case 0:     //Markets tab
                        if (mBinding.tabLayoutTop.getSelectedTabPosition() == 0) {
                            setMarketviewFragment();
                        } else {
                            setWatchlistFragment();
                        }
                        break;
                    case 1:     //Portfolio tab
                        setPortfolioFragment();
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

                switch (mBinding.tabLayoutTop.getSelectedTabPosition()) {
                    case 0:  //Market View tab
                        setMarketviewFragment();
                        break;
                    case 1:  //Watchlist tab
                        setWatchlistFragment();
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

        //Store which fragment was last displayed before we change the fragment
        setValueOfLastFragmentDisplayed(fm);

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

    //Helper method that sets the correct value for lastFragmentDisplayed
    private void setValueOfLastFragmentDisplayed(FragmentManager fm) {
        Fragment fragment = fm.findFragmentById(R.id.frag_main);

        if (fragment == null || fragment instanceof MarketviewFragment) {
            lastFragmentDisplayed = FRAG_MARKETVIEW;  //Default state is marketview fragment if no previous fragment exists
        } else if (fragment instanceof WatchlistFragment) {
            lastFragmentDisplayed = FRAG_WATCHLIST;
        } else if (fragment instanceof  PortfolioFragment) {
            lastFragmentDisplayed = FRAG_PORTFOLIO;
        } else if (fragment instanceof DetailsFragment) {
            lastFragmentDisplayed = FRAG_DETAILS;
        }
    }

    private void setMarketviewFragment() {
        mBinding.tabLayoutTop.setVisibility(View.VISIBLE);
        mBinding.tabLayoutBottom.setVisibility(View.VISIBLE);
        Fragment fragment = new MarketviewFragment(cryptos);
        setFragment(fragment);

        //Hide the back button if it was shown
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setPortfolioFragment() {
        mBinding.tabLayoutTop.setVisibility(View.GONE);
        mBinding.tabLayoutBottom.setVisibility(View.VISIBLE);
        Fragment fragment = new PortfolioFragment();
        setFragment(fragment);

        //Hide the back button if it was shown
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setWatchlistFragment() {
        mBinding.tabLayoutTop.setVisibility(View.VISIBLE);
        mBinding.tabLayoutBottom.setVisibility(View.VISIBLE);
        Fragment fragment = new WatchlistFragment();
        setFragment(fragment);

        //Hide the back button if it was shown
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setDetailsFragment(Crypto crypto) {
        mBinding.tabLayoutTop.setVisibility(View.GONE);
        mBinding.tabLayoutBottom.setVisibility(View.GONE);
        Fragment fragment = new DetailsFragment(crypto);
        setFragment(fragment);

        //Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(crypto.getName());
    }

    //Set behavior of actionbar back button
    //Back button only displays on the main activity if we are in details fragment
    //TODO - Make this work for the device back button as well
    @Override
    public boolean onSupportNavigateUp(){
        //TODO - Fix so it goes back to watchlist if selected from watchlist
        if (lastFragmentDisplayed == FRAG_WATCHLIST) {
            setWatchlistFragment();
        } else {
            setMarketviewFragment();
        }
        return true;
    }

    @Override
    public void onMarketItemClick(Crypto crypto) {
        //Display the details tab for the selected crypto
        //Also hide the top and bottom tabs
        setDetailsFragment(crypto);
    }
}
