package com.shuayb.capstone.android.crypfolio.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarketRecyclerViewAdapter
        extends RecyclerView.Adapter<MarketRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Crypto> cryptos;
    private Context mContext;

    private MarketItemClickListener marketItemClickListener;

    public interface MarketItemClickListener {
        void onMarketItemClick(Crypto crypto);
    }

    public MarketRecyclerViewAdapter(Context mContext, ArrayList<Crypto> cryptos,
                                     MarketItemClickListener marketItemClickListener) {
        this.mContext = mContext;
        this.cryptos = cryptos;
        this.marketItemClickListener = marketItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Picasso.get().load(cryptos.get(position).getImage()).into(holder.logoImage);
        holder.tvName.setText(cryptos.get(position).getName());
        holder.tvMarketcap.setText(cryptos.get(position).getFormattedMarketcapShort());
        holder.tvPrice.setText("$" + cryptos.get(position).getFormattedPrice());

    }

    @Override
    public int getItemCount() {
        return cryptos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView logoImage;
        TextView tvName;
        TextView tvMarketcap;
        TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImage = itemView.findViewById(R.id.logo_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMarketcap = itemView.findViewById(R.id.tv_marketcap);
            tvPrice = itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            marketItemClickListener.onMarketItemClick(cryptos.get(getAdapterPosition()));
        }
    }
}
