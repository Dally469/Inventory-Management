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
import com.karake.EReport.models.Client;
import com.karake.EReport.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Product> product;
    private List<Product> productListFiltered;
    private ProductAdapterListener listener;
    public static  boolean isFiltered = false;
    EReportDB_Helper db_helper;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,quantity,txt_status,type,price;
        public LinearLayout lnl_row_client,lnl_status;

        public MyViewHolder(View view) {
            super(view);
            db_helper = new EReportDB_Helper(context);
            name = view.findViewById(R.id.txt_client_name);
            price = view.findViewById(R.id.txt_product_price);
            type = view.findViewById(R.id.txt_product_type);
            quantity = view.findViewById(R.id.txt_client_phone);
            lnl_status = view.findViewById(R.id.lnl_status);
            txt_status = view.findViewById(R.id.tv_status);
            lnl_row_client = view.findViewById(R.id.lnl_row_client);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onClientSelected(productListFiltered.get(getAdapterPosition()));
                }
            });
            lnl_row_client.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send selected contact in callback
                    listener.onClientSelected(productListFiltered.get(getAdapterPosition()));
                }
            });

        }
    }

    public ProductAdapter(Context context, List<Product> product, ProductAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.product = product;
        this.productListFiltered = product;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product product = productListFiltered.get(position);
        holder.name.setText(product.getName());
        holder.type.setText(product.getType());
        holder.price.setText(product.getPrice() + " Frw");
        holder.quantity.setText(product.getQuantity() + " available");

        if (product.getQuantity() >= 35000){
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_navy_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorNavy));
            holder.txt_status.setText("Full Stock");
        }else if (product.getQuantity() < 35000 && product.getQuantity() >= 15000){
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_orange_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorOrange));
            holder.txt_status.setText("Lower Stock");
        }else if (product.getQuantity() < 15000 && product.getQuantity() >= 1000){
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_gray_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.txt_status.setText("Critical Low Stock");
        }else{
            holder.lnl_status.setBackground(context.getResources().getDrawable(R.drawable.status_red_color));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.txt_status.setText("Empty Stock");
        }
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = product;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : product) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Product>) filterResults.values;
                isFiltered = true;
                notifyDataSetChanged();
            }
        };
    }

    public interface ProductAdapterListener {
        void onClientSelected(Product product);
    }
}