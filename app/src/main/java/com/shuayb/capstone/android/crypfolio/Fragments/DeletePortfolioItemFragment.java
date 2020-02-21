package com.shuayb.capstone.android.crypfolio.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shuayb.capstone.android.crypfolio.POJOs.PortfolioItem;
import com.shuayb.capstone.android.crypfolio.databinding.DeletePortfolioItemFragmentBinding;

public class DeletePortfolioItemFragment extends DialogFragment {
    private static final String TAG = "DeletePortfolioItemFragment";

    private static final String KEY_BUNDLE_PORTFOLIO_ITEM = "portfolio_item_key";

    DeletePortfolioItemFragmentBinding mBinding;
    PortfolioItem portfolioItem;

    DeletePortfolioDialogListener mCallback;

    public interface DeletePortfolioDialogListener {
        public void onDeleteClicked(PortfolioItem item);
        public void onCancelDeleteClicked();
        public void onDismissed();
    }

    public static final DeletePortfolioItemFragment newInstance(PortfolioItem portfolioItem) {
        DeletePortfolioItemFragment f = new DeletePortfolioItemFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(KEY_BUNDLE_PORTFOLIO_ITEM, portfolioItem);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        portfolioItem = getArguments().getParcelable(KEY_BUNDLE_PORTFOLIO_ITEM);

        try {
            mCallback = (DeletePortfolioDialogListener)getTargetFragment();

        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().toString() + " must implement DeletePortfolioDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DeletePortfolioItemFragmentBinding.inflate(inflater, container, false);

        initViews();



        return mBinding.getRoot();
    }

    private void initViews() {
        mBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCancelDeleteClicked();
            }
        });

        mBinding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onDeleteClicked(portfolioItem);
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mCallback.onDismissed();
    }
}
