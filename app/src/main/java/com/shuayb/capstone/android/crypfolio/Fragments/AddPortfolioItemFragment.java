package com.shuayb.capstone.android.crypfolio.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shuayb.capstone.android.crypfolio.DataUtils.RandomUtils;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.AddPortfolioItemFragmentBinding;

public class AddPortfolioItemFragment extends DialogFragment {
    private static final String TAG = "AddPortfolioItemFragment";

    private static final String KEY_BUNDLE_CRYPTO = "crypto_key";

    AddPortfolioItemFragmentBinding mBinding;

    Crypto crypto;
    PortfolioItemDialogListener mCallback;

    public interface PortfolioItemDialogListener {
        public void onSubmitPressed(String cryptoId, double amount, double purchasePrice);
        public void onCancelPressed();
        public void onDismissed();
    }

    public static final AddPortfolioItemFragment newInstance(Crypto data) {
        AddPortfolioItemFragment f = new AddPortfolioItemFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(KEY_BUNDLE_CRYPTO, data);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (PortfolioItemDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PortfolioItemDialogListener");
        }
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
        mBinding.priceEditText.setText(RandomUtils.getFormattedCurrencyAmount(crypto.getCurrentPrice()));

        mBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double amount = Double.parseDouble(mBinding.amountEditText.getText().toString());
                    double purchasePrice = Double.parseDouble(mBinding.priceEditText.getText().toString());
                    mCallback.onSubmitPressed(crypto.getId(), amount, purchasePrice);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid entries - try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCancelPressed();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mCallback.onDismissed();
    }
}
