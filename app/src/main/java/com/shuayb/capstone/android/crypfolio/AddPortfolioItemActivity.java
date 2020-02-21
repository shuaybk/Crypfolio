package com.shuayb.capstone.android.crypfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.shuayb.capstone.android.crypfolio.CustomAdapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.Fragments.AddPortfolioItemFragment;
import com.shuayb.capstone.android.crypfolio.databinding.ActivityAddPortfolioItemBinding;

import java.util.ArrayList;

public class AddPortfolioItemActivity extends AppCompatActivity
        implements MarketRecyclerViewAdapter.MarketItemClickListener {


    private static final String TAG = "AddPortfolioItemActivity";

    private static final String KEY_BUNDLE_ARRAYLIST = "crypto_list";

    private ArrayList<Crypto> cryptos;
    private ActivityAddPortfolioItemBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_item);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_portfolio_item);

        Intent parentIntent = getIntent();
        cryptos = parentIntent.getParcelableArrayListExtra(KEY_BUNDLE_ARRAYLIST);

        initViews();
    }

    private void initViews() {
        mBinding.backgroundCover.setVisibility(View.GONE);
        mBinding.backgroundCover.setAlpha(0.5f);

        MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(this, cryptos, this);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMarketItemClick(Crypto crypto) {
        AddPortfolioItemFragment fragment = AddPortfolioItemFragment.newInstance(crypto);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_add, fragment)
                .commit();

        mBinding.backgroundCover.setVisibility(View.VISIBLE);
        disableRecyclerViewInteraction();
    }
    
    public void disableRecyclerViewInteraction() {
        mBinding.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void enableRecyclerViewInteraction() {
        mBinding.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
}
