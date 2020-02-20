package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shuayb.capstone.android.crypfolio.R;

public class PortfolioFragment extends Fragment {
    private static final String TAG = "PortfolioFragment";


    public static final PortfolioFragment newInstance() {
        PortfolioFragment f = new PortfolioFragment();
        //Bundle bundle = new Bundle(1);
        //f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.portfolio_fragment, container, false);

        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
