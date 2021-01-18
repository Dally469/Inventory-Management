package com.karake.EReport.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karake.EReport.R;
import com.karake.EReport.adapters.SalesAdapter;
import com.karake.EReport.adapters.SingleClientSalesAdapter;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Client;
import com.karake.EReport.models.Sale;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ClientProfile extends AppCompatActivity implements SingleClientSalesAdapter.SalesAdapterListener {
    TextView name,phone,address,company_name,compant_tin;
    Client client;
    EReportDB_Helper db_helper;
    PrefManager manager;
    LinearLayout lnl_call,lnl_address;
    Toolbar toolbar;

    RecyclerView sales_recyclerView;
    SingleClientSalesAdapter salesAdapter;
    List<Sale> salesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        db_helper = new EReportDB_Helper(this);
        manager = new PrefManager(getApplicationContext());
        client = db_helper.getClientByID(manager.getCurrentClient());
        // toolbar info
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(client.getName());
//        toolbar.setSubtitle(client.getCompantName());
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        name = findViewById(R.id.tv_name);
        phone = findViewById(R.id.tv_phone);
        address = findViewById(R.id.tv_address);
        compant_tin = findViewById(R.id.tv_company_tin);
        company_name = findViewById(R.id.tv_company_name);

        lnl_call = findViewById(R.id.lnl_phone_call);
        lnl_address = findViewById(R.id.lnl_address);

        name.setText(client.getName());
        phone.setText("+25"+client.getPhone());
        address.setText(client.getAddress());
        company_name.setText(client.getCompantName());
        compant_tin.setText("TIN = " + client.getTin());

        lnl_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent();
            }
        });

        sales_recyclerView = findViewById(R.id.recyclerviewSales);
        salesAdapter = new SingleClientSalesAdapter(getApplicationContext(), salesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sales_recyclerView.setLayoutManager(mLayoutManager);
        sales_recyclerView.setItemAnimator(new DefaultItemAnimator());
        sales_recyclerView.setAdapter(salesAdapter);
        getAllSalesByClient();
    }

    private void callIntent(){
        Intent call = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", client.getPhone(), null));
        call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(call);
    }

    private void getAllSalesByClient() {
        salesList.clear();
        salesList.addAll(db_helper.getAllSalesByClientID(client.getId()));
        salesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.call_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_call){
            callIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSalesSelected(Sale sale) {
        Intent intent = new Intent(getApplicationContext(),SalesDetails.class);

        new PrefManager(getApplicationContext()).setCurrentSales(sale.getId());
        startActivity(intent);

    }

    @Override
    public void onHoldSalesSelected(final Sale sale) {
        final AlertDialog dialog;
        final EditText ed_paid;
        final TextView tv_name,tv_quantity,tv_tot_amount,tv_paid_amount,tv_rest_amount,tv_title_rest;
        final Button btnSave;
        final LinearLayout lnl_succuess;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View viewDialog = layoutInflater.inflate(R.layout.update_sales_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btnSave = viewDialog.findViewById(R.id.btn_save);

        tv_name = viewDialog.findViewById(R.id.tv_product_name);
        tv_quantity = viewDialog.findViewById(R.id.tv_product_quantity);
        tv_tot_amount = viewDialog.findViewById(R.id.total_amount);
        tv_paid_amount = viewDialog.findViewById(R.id.paid_amount);
        tv_rest_amount = viewDialog.findViewById(R.id.rest_amount);
        lnl_succuess = viewDialog.findViewById(R.id.lnl_success);
        ed_paid = viewDialog.findViewById(R.id.product_price_paid);
        tv_title_rest = viewDialog.findViewById(R.id.tv_title_rest);

        tv_name.setText(sale.getProduct_name());
        tv_quantity.setText(" " + sale.getQuantity() + " Items");
        tv_tot_amount.setText(sale.getCurrent_price_id()+ " Frw");
        tv_paid_amount.setText(sale.getPrice_paid()+ " Frw");
        tv_rest_amount.setText(sale.getPrice_remain()+ " Frw");

        builder.setTitle("Completing sales payment");
        builder.setCancelable(true);
        builder.setView(viewDialog);

        if (sale.getPrice_paid() >= sale.getCurrent_price_id()){
            builder.setTitle("Payment Complete");
            ed_paid.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            tv_title_rest.setVisibility(View.GONE);
            tv_rest_amount.setVisibility(View.GONE);
        }else {
            ed_paid.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            lnl_succuess.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_paid.getText().toString().isEmpty()){
                    ed_paid.setError("Please Enter Paid amount");
                    ed_paid.requestFocus();
                }else {
                    Sale sales = db_helper.getSaleByID(sale.getId());
                    sales.setPrice_paid(sales.getPrice_paid() + Integer.parseInt(ed_paid.getText().toString()));
                    sales.setPrice_remain(sales.getPrice_remain() - Integer.parseInt(ed_paid.getText().toString()));


                    if(db_helper.updateSale(sales)){
                        // SweetAlert
                        Toast.makeText(getApplicationContext(), "Product Update successful", Toast.LENGTH_SHORT).show();
                        Intent sold = new Intent(getApplicationContext(),Sales.class);
                        startActivity(sold);
                        getAllSalesByClient();
                        finish();


                    }else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
        finish();
    }
}