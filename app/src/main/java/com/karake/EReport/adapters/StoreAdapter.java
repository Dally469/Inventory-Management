package com.karake.EReport.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Product;
import com.karake.EReport.models.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Store> stores;
    private List<Store> storesListFiltered;
    private StoreAdapterListener listener;
    public static  boolean isFiltered = false;
    EReportDB_Helper db_helper;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,quantity,txt_status,quantity_request;
        public LinearLayout lnl_row_client,lnl_status;

        public MyViewHolder(View view) {
            super(view);
            db_helper = new EReportDB_Helper(context);
            name = view.findViewById(R.id.txt_product_name);
            quantity = view.findViewById(R.id.txt_product_quantity);
            quantity_request = view.findViewById(R.id.txt_product_quantity_request);
            txt_status = view.findViewById(R.id.tv_status);
            lnl_status = view.findViewById(R.id.lnl_status);
            lnl_row_client = view.findViewById(R.id.lnl_row_client);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onItemSelected(storesListFiltered.get(getAdapterPosition()));
                }
            });
            lnl_row_client.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send selected contact in callback
                    listener.onItemSelected(storesListFiltered.get(getAdapterPosition()));
                }
            });

        }
    }

    public StoreAdapter(Context context, List<Store> stores, StoreAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.stores = stores;
        this.storesListFiltered = stores;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_stock_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Store store = storesListFiltered.get(position);
        holder.name.setText(store.getProduct_name());
        holder.quantity.setText(store.getProduct_quantity()+ " Item");
        holder.quantity_request.setText(store.getProduct_quantity_request() + " Item");

        if (store.getProduct_quantity() >= 350){
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_navy_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorNavy));
            holder.txt_status.setText("Full Stock");
        }else if (store.getProduct_quantity() < 350 && store.getProduct_quantity() >= 15000){
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_orange_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorOrange));
            holder.txt_status.setText("Lower Stock");
        }else if (store.getProduct_quantity() < 150 && store.getProduct_quantity() >= 10){
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_gray_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.txt_status.setText("Low Stock");
        }else{
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_red_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.txt_status.setText("Empty Stock");
        }
    }

    @Override
    public int getItemCount() {
        return storesListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    storesListFiltered = stores;
                } else {
                    List<Store> filteredList = new ArrayList<>();
                    for (Store row : stores) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProduct_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    storesListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = storesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                storesListFiltered = (ArrayList<Store>) filterResults.values;
                isFiltered = true;
                notifyDataSetChanged();
            }
        };
    }

    public interface StoreAdapterListener {
        void onItemSelected(Store store);
    }
}