package com.karake.EReport.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karake.EReport.R;
import com.karake.EReport.adapters.ClientAdapter;
import com.karake.EReport.adapters.DispatchAdapter;
import com.karake.EReport.adapters.SingleClientSalesAdapter;
import com.karake.EReport.adapters.StoreAdapter;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Client;
import com.karake.EReport.models.Sale;
import com.karake.EReport.models.Store;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Stock extends AppCompatActivity implements DispatchAdapter.DispatchAdapterListener {
    EReportDB_Helper db_helper = new EReportDB_Helper(this);
    Toolbar toolbar;

    //for list Users
    RecyclerView recyclerView;
    DispatchAdapter dispatchAdapter;
    List<Sale> salesList = new ArrayList<>();

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //for toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Checking Dispatch ");
        toolbar.setTitleTextColor(Color.WHITE);
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

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewRequestDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerviewDispatch);
        dispatchAdapter = new DispatchAdapter(getApplicationContext(), salesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dispatchAdapter);

        getAllDispatch();
    }

    private void getAllDispatch() {
        salesList.clear();
        salesList.addAll(db_helper.getAllSales());
        dispatchAdapter.notifyDataSetChanged();
    }

    private void AddNewRequestDialog() {
        final AlertDialog dialog;

        final Button btnSave;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View viewDialog = layoutInflater.inflate(R.layout.scan_qr_code_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btnSave = viewDialog.findViewById(R.id.btn_save);


        builder.setTitle("Scan QR Code");
        builder.setCancelable(true);
        builder.setView(viewDialog);



        dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    public void onDispatchSelected(final Sale sale) {
        final AlertDialog dialog;
        final Button btnSave,btnClose;
        final ImageView img_success;
        final TextView txt_success;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View viewDialog = layoutInflater.inflate(R.layout.update_delivery_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btnSave = viewDialog.findViewById(R.id.btn_save);
        btnClose = viewDialog.findViewById(R.id.btn_close);
        img_success = viewDialog.findViewById(R.id.img_success);
        txt_success = viewDialog.findViewById(R.id.txt_success);

        if (sale.getStatus().equals("1")) {
            img_success.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
            txt_success.setText("Delivery Successful \n Product for " + sale.getClient_name());
            btnSave.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
        }else {
            builder.setTitle(sale.getClient_name());
        }


        builder.setCancelable(true);
        builder.setView(viewDialog);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sold = new Intent(getApplicationContext(),Stock.class);
                startActivity(sold);
                getAllDispatch();
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sale sales = db_helper.getSaleByID(sale.getId());
                int status = 1;
                int utc  = (int)(new Date().getTime()/1000);
                sales.setStatus(Integer.toString(status));
                sales.setUpdated_at(Integer.toString(utc));

                if(db_helper.updateSale(sales)){
                    // SweetAlert
                    Toast.makeText(getApplicationContext(), "Product Delivery Successful", Toast.LENGTH_SHORT).show();
                    Intent sold = new Intent(getApplicationContext(),Stock.class);
                    startActivity(sold);
                    getAllDispatch();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });



        dialog = builder.create();
        dialog.show();
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        getAllDispatch();

    }
}