package com.shuayb.capstone.android.crypfolio.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuayb.capstone.android.crypfolio.DataUtils.RandomUtils;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.POJOs.PortfolioItem;
import com.shuayb.capstone.android.crypfolio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PortfolioRecyclerViewAdapter
        extends RecyclerView.Adapter<PortfolioRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "PortfolioRecyclerViewAdapter";

    private ArrayList<PortfolioItem> portfolioItems;
    private Context mContext;

    private PortfolioItemClickListener portfolioItemClickListener;

    public interface PortfolioItemClickListener {
        void onPortfolioItemClick(PortfolioItem portfolioItem);
        void onPortfolioItemLongClick(PortfolioItem portfolioItem);
    }

    public PortfolioRecyclerViewAdapter(Context mContext, ArrayList<PortfolioItem> portfolioItems,
                                        PortfolioItemClickListener portfolioItemClickListener) {
        this.mContext = mContext;
        this.portfolioItems = portfolioItems;
        this.portfolioItemClickListener = portfolioItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        PortfolioItem item = portfolioItems.get(position);

        String netChange = RandomUtils.getNetChangeAmount(item.getAmount(), item.getAvgPrice(), item.getCurrentPrice())
                + " (" + RandomUtils.getNetChangePercentage(item.getAmount(), item.getAvgPrice(), item.getCurrentPrice())
                + ")";

        String amountPrice = RandomUtils.roundToReasonableValue(item.getAmount()) + " | "
                + "$" + RandomUtils.getFormattedCurrencyAmount(item.getCurrentPrice());

        Picasso.get().load(item.getImage()).into(holder.logoImage);
        holder.nameText.setText(item.getName());
        holder.priceTotalText.setText("$" + RandomUtils.getFormattedCurrencyAmount(item.getCurrentPrice()));
        holder.netChangeText.setText(netChange);
        holder.amountPriceText.setText(amountPrice);
    }

    @Override
    public int getItemCount() {
        return portfolioItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        ImageView logoImage;
        TextView nameText;
        TextView priceTotalText;
        TextView netChangeText;
        TextView amountPriceText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImage = itemView.findViewById(R.id.logo_image);
            nameText = itemView.findViewById(R.id.tv_name);
            priceTotalText = itemView.findViewById(R.id.price_total);
            netChangeText = itemView.findViewById(R.id.net_change_text);
            amountPriceText = itemView.findViewById(R.id.amount_price_text);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PortfolioItem item = portfolioItems.get(getAdapterPosition());

            portfolioItemClickListener.onPortfolioItemClick(item);
        }

        @Override
        public boolean onLongClick(View v) {
            PortfolioItem item = portfolioItems.get(getAdapterPosition());

            portfolioItemClickListener.onPortfolioItemLongClick(item);
            return true;
        }
    }
}