package com.karake.EReport.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Client> client;
    private List<Client> clientListFiltered;
    private ClientAdapterListener listener;
    public static  boolean isFiltered = false;
    EReportDB_Helper db_helper;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,phone;
        public LinearLayout lnl_row_client,lnl_call;

        public MyViewHolder(View view) {
            super(view);
            db_helper = new EReportDB_Helper(context);
            name = view.findViewById(R.id.txt_client_name);
            phone = view.findViewById(R.id.txt_client_phone);
            lnl_call = view.findViewById(R.id.lnl_call_client);
            lnl_row_client = view.findViewById(R.id.lnl_row_client);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onClientSelected(clientListFiltered.get(getAdapterPosition()));
                }
            });
            lnl_row_client.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send selected contact in callback
                    listener.onClientSelected(clientListFiltered.get(getAdapterPosition()));
                }
            });
            lnl_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent call = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", clientListFiltered.get(getAdapterPosition()).getPhone(), null));
                    call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(call);
                }
            });

        }
    }

    public ClientAdapter(Context context, List<Client> client, ClientAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.client = client;
        this.clientListFiltered = client;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_client, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Client client = clientListFiltered.get(position);
        holder.name.setText(client.getName());
        holder.phone.setText(client.getPhone());

    }

    @Override
    public int getItemCount() {
        return clientListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    clientListFiltered = client;
                } else {
                    List<Client> filteredList = new ArrayList<>();
                    for (Client row : client) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    clientListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = clientListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clientListFiltered = (ArrayList<Client>) filterResults.values;
                isFiltered = true;
                notifyDataSetChanged();
            }
        };
    }

    public interface ClientAdapterListener {
        void onClientSelected(Client client);
    }
}