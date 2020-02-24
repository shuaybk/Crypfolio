package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.shuayb.capstone.android.crypfolio.CustomAdapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DataViewModel;
import com.shuayb.capstone.android.crypfolio.MainActivity;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.MarketviewFragmentBinding;

import java.util.ArrayList;

public class MarketviewFragment extends Fragment {
    private static final String TAG = "MarketviewFragment";

    private MarketviewFragmentBinding mBinding;
    private DataViewModel mData;
    private Observer<ArrayList<Crypto>> cryptoObserver;
    private MutableLiveData<ArrayList<Crypto>> cryptosLD;


    //Create new instance of the fragment here instead of using a custom constructor
    //Best practice is not to overwrite the default constructor (otherwise will cause errors)
    public static final MarketviewFragment newInstance() {
        MarketviewFragment f = new MarketviewFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = MarketviewFragmentBinding.inflate(inflater, container, false);

        //showLoadingScreen();

        mData = ViewModelProviders.of(getActivity()).get(DataViewModel.class);

        cryptoObserver = new Observer<ArrayList<Crypto>>() {
            @Override
            public void onChanged(ArrayList<Crypto> cryptos) {
                setRecyclerView();
                //showMainScreen();
            }
        };
        cryptosLD = mData.getCryptos();
        cryptosLD.observe(this, cryptoObserver);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        if (cryptoObserver != null) {
            cryptosLD.removeObserver(cryptoObserver);
            cryptoObserver = null;
        }
        super.onDestroyView();
    }

    private void setRecyclerView() {
        if (mBinding.recyclerView.getAdapter() == null) {
            MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(getContext(), mData.getCryptos().getValue(), (MainActivity) getContext());
            mBinding.recyclerView.setAdapter(adapter);
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            MarketRecyclerViewAdapter adapter = (MarketRecyclerViewAdapter)(mBinding.recyclerView.getAdapter());
            adapter.updateCryptos(mData.getCryptos().getValue());
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void showLoadingScreen() {
        mBinding.mainContainer.setVisibility(View.GONE);
        mBinding.loadingContainer.setVisibility(View.VISIBLE);
    }

    private void showMainScreen() {
        mBinding.mainContainer.setVisibility(View.VISIBLE);
        mBinding.loadingContainer.setVisibility(View.GONE);
    }
}
