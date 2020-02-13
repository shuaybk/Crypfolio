package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shuayb.capstone.android.crypfolio.R;

public class WatchlistFragment extends Fragment {
    private static final String TAG = "WatchlistFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);

        return view;
    }
}
