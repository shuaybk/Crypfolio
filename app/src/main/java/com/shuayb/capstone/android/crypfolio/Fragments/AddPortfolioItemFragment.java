package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.AddPortfolioItemFragmentBinding;

public class AddPortfolioItemFragment extends Fragment {
    private static final String TAG = "AddPortfolioItemFragment";

    private static final String KEY_BUNDLE_CRYPTO = "crypto_key";

    AddPortfolioItemFragmentBinding mBinding;

    Crypto crypto;

    public static final AddPortfolioItemFragment newInstance(Crypto data) {
        AddPortfolioItemFragment f = new AddPortfolioItemFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(KEY_BUNDLE_CRYPTO, data);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crypto = getArguments().getParcelable(KEY_BUNDLE_CRYPTO);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = AddPortfolioItemFragmentBinding.inflate(inflater, container, false);

        initViews();

        return mBinding.getRoot();
    }

    private void initViews() {
        mBinding.coinNameText.setText(crypto.getName());

        mBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = mBinding.submitButton.getText().toString();
                Toast.makeText(getContext(), "You added " + amount + " " + crypto.getName() + " to your portfolio", Toast.LENGTH_SHORT).show();
                //TODO - This should return a result to calling main activity
            }
        });
    }
}
