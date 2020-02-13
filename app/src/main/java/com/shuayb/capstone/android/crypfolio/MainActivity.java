package com.shuayb.capstone.android.crypfolio;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.shuayb.capstone.android.crypfolio.Fragments.MarketsFragment;
import com.shuayb.capstone.android.crypfolio.Fragments.PortfolioFragment;
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
        Fragment fragment = new MarketsFragment();
        setFragment(fragment);

        setupTabs();

    }

    //Method to set up the behaviour of the tabs
    //The tabs determine which fragment will be displayed
    //Except for settings, which launches a new activity
    private void setupTabs() {
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;

                switch (mBinding.tabLayout.getSelectedTabPosition()) {
                    case 0:
                        fragment = new MarketsFragment();
                        setFragment(fragment);
                        break;
                    case 1:
                        fragment = new PortfolioFragment();
                        setFragment(fragment);
                        break;
                    case 2:
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
