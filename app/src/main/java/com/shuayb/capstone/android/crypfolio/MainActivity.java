package com.shuayb.capstone.android.crypfolio;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.shuayb.capstone.android.crypfolio.Fragments.MarketviewFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.PortfolioFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.WatchlistFragment;
import com.shuayb.capstone.android.crypfolio.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding mBinding;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //On initial startup, display the markets fragment
        Fragment fragment = new MarketviewFragment();
        setFragment(fragment);

        setupMainTabs();
        setupTopTabs();
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
                            fragment = new MarketviewFragment();
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
                        fragment = new MarketviewFragment();
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
}
