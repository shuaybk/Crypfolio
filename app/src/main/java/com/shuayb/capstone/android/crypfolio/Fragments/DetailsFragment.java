package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shuayb.capstone.android.crypfolio.POJOs.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.DetailsFragmentBinding;

public class DetailsFragment extends Fragment {

    DetailsFragmentBinding mBinding;
    Crypto crypto;

    public DetailsFragment(Crypto crypto) {
        this.crypto = crypto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DetailsFragmentBinding.inflate(inflater, container, false);

        initViews();

        return mBinding.getRoot();
    }


    //Helper method to initialize the views with crypto information
    private void initViews() {
        mBinding.symbolText.setText(crypto.getSymbol());
        mBinding.nameText.setText(crypto.getName());
        mBinding.marketcapText.setText("Market Cap: " + crypto.getFormattedMarketcapFull());
    }
}
