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

    private static final String KEY_BUNDLE_MARKETVIEW_FRAGMENT = "marketview_fragment";
    private static final String KEY_BUNDLE_WATCHLIST_FRAGMENT = "watchlist_fragment";
    private static final String KEY_BUNDLE_PORTFOLIO_FRAGMENT = "portfolio_fragment";
    private static final String KEY_BUNDLE_DETAILS_FRAGMENT = "details_fragment";
    private static final String KEY_BUNDLE_LAST_FRAGMENT_DISPLAYED = "last_fragment";
    private static final String KEY_BUNDLE_CRYPTO_LIST = "crypto_list";
    private static final String KEY_BUNDLE_TOP_TAB_POS = "top_tab_position";
    private static final String KEY_BUNDLE_BOTTOM_TAB_POS = "bottom_tab_position";

    private static final int FRAG_MARKETVIEW = 1;
    private static final int FRAG_WATCHLIST = 2;
    private static final int FRAG_PORTFOLIO = 3;
    private static final int FRAG_DETAILS = 4;

    private ActivityMainBinding mBinding;
    private TabLayout mTabLayout;
    private int lastFragmentDisplayed = 0;  //Keeps track of what fragment was last displayed
    private MarketviewFragment marketviewFragment;
    private WatchlistFragment watchlistFragment;
    private PortfolioFragment portfolioFragment;
    private DetailsFragment detailsFragment;

    ArrayList<Crypto> cryptos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        if (savedInstanceState != null) {
            restoreSetup(savedInstanceState);
        } else {
            initialSetup();
        }

    }

    private void initialSetup() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.getUrlForMarketviewData(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse got this data: " + response);
                cryptos = JsonUtils.convertJsonToCryptoList(response);

                marketviewFragment = MarketviewFragment.newInstance(cryptos);
                watchlistFragment = WatchlistFragment.newInstance();
                portfolioFragment = PortfolioFragment.newInstance();
                detailsFragment = DetailsFragment.newInstance(null);

                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .add(R.id.frag_main, marketviewFragment)
                        .commit();
                fm.beginTransaction()
                        .detach(marketviewFragment)
                        .add(R.id.frag_main, watchlistFragment)
                        .commit();
                fm.beginTransaction()
                        .detach(watchlistFragment)
                        .add(R.id.frag_main, portfolioFragment)
                        .commit();
                fm.beginTransaction()
                        .detach(portfolioFragment)
                        .add(R.id.frag_main, detailsFragment)
                        .commit();
                fm.beginTransaction()
                        .detach(detailsFragment)
                        .commit();

                setMarketviewFragment();

                setupBottomTabs(0);
                setupTopTabs(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error!!!!!!!! " + error.getMessage());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void restoreSetup(Bundle savedInstanceState) {


        FragmentManager fm = getSupportFragmentManager();

        marketviewFragment = (MarketviewFragment)fm.getFragment(savedInstanceState, KEY_BUNDLE_MARKETVIEW_FRAGMENT);
        watchlistFragment = (WatchlistFragment)fm.getFragment(savedInstanceState, KEY_BUNDLE_WATCHLIST_FRAGMENT);
        portfolioFragment = (PortfolioFragment)fm.getFragment(savedInstanceState, KEY_BUNDLE_PORTFOLIO_FRAGMENT);
        detailsFragment = (DetailsFragment)fm.getFragment(savedInstanceState, KEY_BUNDLE_DETAILS_FRAGMENT);

        lastFragmentDisplayed = savedInstanceState.getInt(KEY_BUNDLE_LAST_FRAGMENT_DISPLAYED);
        cryptos = savedInstanceState.getParcelableArrayList(KEY_BUNDLE_CRYPTO_LIST);
        int topTabPosition = savedInstanceState.getInt(KEY_BUNDLE_TOP_TAB_POS);
        int bottomTabPosition = savedInstanceState.getInt(KEY_BUNDLE_BOTTOM_TAB_POS);

        setupBottomTabs(bottomTabPosition);
        setupTopTabs(topTabPosition);

        Fragment currFrag = fm.findFragmentById(R.id.frag_main);

        if (currFrag instanceof MarketviewFragment) {
            setMarketviewFragmentViews();
        } else if (currFrag instanceof WatchlistFragment) {
            setWatchlistFragmentViews();
        } else if (currFrag instanceof PortfolioFragment) {
            setPortfolioFragmentViews();
        } else if (currFrag instanceof DetailsFragment) {
            setDetailsFragmentViews();
        }

    }


    //Method to set up the behaviour of the tabs
    //The tabs determine which fragment will be displayed
    //Except for settings, which launches a new activity
    private void setupBottomTabs(int pos) {
        if (pos >= 0) {
            mBinding.tabLayoutBottom.getTabAt(pos).select();
        }
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


    private void setupTopTabs(int pos) {
        if (pos >= 0) {
            mBinding.tabLayoutTop.getTabAt(pos).select();
        }
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

        Fragment currFrag = fm.findFragmentById(R.id.frag_main);

        if (currFrag != null) {
            fm.beginTransaction()
                    .detach(currFrag)
                    .attach(fragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .attach(fragment)
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
        setFragment(marketviewFragment);
        setMarketviewFragmentViews();
    }

    private void setPortfolioFragment() {
        setFragment(portfolioFragment);
        setPortfolioFragmentViews();
    }

    private void setWatchlistFragment() {
        setFragment(watchlistFragment);
        setWatchlistFragmentViews();
    }

    private void setDetailsFragment(Crypto crypto) {
        setFragment(detailsFragment);
        setDetailsFragmentViews();
    }


    private void setMarketviewFragmentViews() {
        mBinding.tabLayoutTop.setVisibility(View.VISIBLE);
        mBinding.tabLayoutBottom.setVisibility(View.VISIBLE);

        //Hide the back button if it was shown
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setPortfolioFragmentViews() {
        mBinding.tabLayoutTop.setVisibility(View.GONE);
        mBinding.tabLayoutBottom.setVisibility(View.VISIBLE);

        //Hide the back button if it was shown
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setWatchlistFragmentViews() {
        mBinding.tabLayoutTop.setVisibility(View.VISIBLE);
        mBinding.tabLayoutBottom.setVisibility(View.VISIBLE);

        //Hide the back button if it was shown
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setDetailsFragmentViews() {
        mBinding.tabLayoutTop.setVisibility(View.GONE);
        mBinding.tabLayoutBottom.setVisibility(View.GONE);

        //Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, KEY_BUNDLE_MARKETVIEW_FRAGMENT, marketviewFragment);
        getSupportFragmentManager().putFragment(outState, KEY_BUNDLE_WATCHLIST_FRAGMENT, watchlistFragment);
        getSupportFragmentManager().putFragment(outState, KEY_BUNDLE_PORTFOLIO_FRAGMENT, portfolioFragment);
        getSupportFragmentManager().putFragment(outState, KEY_BUNDLE_DETAILS_FRAGMENT, detailsFragment);

        outState.putInt(KEY_BUNDLE_LAST_FRAGMENT_DISPLAYED, lastFragmentDisplayed);
        outState.putParcelableArrayList(KEY_BUNDLE_CRYPTO_LIST, cryptos);
        outState.putInt(KEY_BUNDLE_TOP_TAB_POS, mBinding.tabLayoutTop.getSelectedTabPosition());
        outState.putInt(KEY_BUNDLE_BOTTOM_TAB_POS, mBinding.tabLayoutBottom.getSelectedTabPosition());
    }
}
