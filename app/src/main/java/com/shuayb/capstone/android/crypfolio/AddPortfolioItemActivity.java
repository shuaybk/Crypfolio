package com.shuayb.capstone.android.crypfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        implements MarketRecyclerViewAdapter.MarketItemClickListener,
                AddPortfolioItemFragment.PortfolioItemDialogListener {

    private static final String TAG = "AddPortfolioItemActivity";

    private static final String FRAGMENT_DIALOG_TAG = "add_portfolio_item_fragment";
    private static final String KEY_CRYPTO_ID = "key_crypto_id";
    private static final String KEY_AMOUNT = "key_amount";
    private static final String KEY_PURCHASE_PRICE = "key_purchase_price";

    private ActivityAddPortfolioItemBinding mBinding;
    private DataViewModel mData;
    MutableLiveData<ArrayList<Crypto>> cryptoLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_portfolio_item);
        mData = ViewModelProviders.of(this).get(DataViewModel.class);
        Intent parentIntent = getIntent();

        if (isConnectedToInternet()) {
            setDataObservers();
        } else {
            showError();
        }
    }

    private void setDataObservers() {
        mData.refreshCryptos(this);

        //Just get the updated info once so we can populate the list
        //We don't need to keep updating it, so unregister the observer after first
        //successful data response
        cryptoLD = mData.getCryptos();
        cryptoLD.observe(this, new Observer<ArrayList<Crypto>>() {
            @Override
            public void onChanged(ArrayList<Crypto> cryptos) {
                if (cryptos.size() > 0) {
                    cryptoLD.removeObserver(this);
                    initViews();
                }
            }
        });
    }

    private void initViews() {
        mBinding.backgroundCover.setVisibility(View.GONE);
        mBinding.backgroundCover.setAlpha(0.5f);

        MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(this, cryptoLD.getValue(), this);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void dismissDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddPortfolioItemFragment fragment = (AddPortfolioItemFragment)fm.findFragmentByTag(FRAGMENT_DIALOG_TAG);
        fragment.dismiss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        return true;
    }

    @Override
    public void onMarketItemClick(Crypto crypto) {
        FragmentManager fm = getSupportFragmentManager();
        AddPortfolioItemFragment fragment = AddPortfolioItemFragment.newInstance(crypto);

        fragment.show(fm, FRAGMENT_DIALOG_TAG);
        mBinding.backgroundCover.setVisibility(View.VISIBLE);
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null) {
            return false;
        }
        return true;
    }

    //Data already verified
    @Override
    public void onSubmitPressed(String cryptoId, double amount, double purchasePrice) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_CRYPTO_ID, cryptoId);
        resultIntent.putExtra(KEY_AMOUNT, amount);
        resultIntent.putExtra(KEY_PURCHASE_PRICE, purchasePrice);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCancelPressed() {
        dismissDialog();
    }

    @Override
    public void onDismissed() {
        mBinding.backgroundCover.setVisibility(View.GONE);
    }

    private void showError() {
        mBinding.mainContainer.setVisibility(View.GONE);
        mBinding.errorContainer.setVisibility(View.VISIBLE);
    }

    private void removeError() {
        mBinding.mainContainer.setVisibility(View.VISIBLE);
        mBinding.errorContainer.setVisibility(View.GONE);
    }
}
