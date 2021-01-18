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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karake.EReport.R;
import com.karake.EReport.adapters.ClientAdapter;
import com.karake.EReport.adapters.StoreAdapter;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Client;
import com.karake.EReport.models.Sale;
import com.karake.EReport.models.Store;

import java.util.ArrayList;
import java.util.List;

public class Stock extends AppCompatActivity implements StoreAdapter.StoreAdapterListener {
    EReportDB_Helper db_helper = new EReportDB_Helper(this);
    Toolbar toolbar;

    //for list Users
    RecyclerView recyclerView;
    StoreAdapter storeAdapter;
    List<Store> storeList = new ArrayList<>();

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //for toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Stock Request");
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

        recyclerView = findViewById(R.id.recyclerviewClient);
        storeAdapter = new StoreAdapter(getApplicationContext(), storeList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storeAdapter);

        //get Members
        getAllProduct();

    }

    private void getAllProduct() {
        storeList.clear();
        storeList.addAll(db_helper.getAllStock());
        storeAdapter.notifyDataSetChanged();
    }

    private void AddNewRequestDialog() {
        final AlertDialog dialog;
        final EditText name,phone,address,company_name,company_tin;
        final Button btnSave;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View viewDialog = layoutInflater.inflate(R.layout.add_new_client_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btnSave = viewDialog.findViewById(R.id.btn_save);
        name = viewDialog.findViewById(R.id.client_name);
        phone = viewDialog.findViewById(R.id.client_phone);
        address = viewDialog.findViewById(R.id.client_address);
        company_name = viewDialog.findViewById(R.id.client_company_name);
        company_tin = viewDialog.findViewById(R.id.client_company_tin);


        builder.setTitle("Create new client");
        builder.setCancelable(true);
        builder.setView(viewDialog);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter Client name", Toast.LENGTH_SHORT).show();

                }else if (phone.getText().toString().isEmpty() && phone.getText().length() == 10){
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Phone number", Toast.LENGTH_SHORT).show();

                }else if (company_name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Company name", Toast.LENGTH_SHORT).show();

                }else if (company_tin.getText().toString().isEmpty() && company_tin.getText().length() == 9){
                    Toast.makeText(getApplicationContext(), "Please Enter valid TIN number", Toast.LENGTH_SHORT).show();

                }else if (address.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter address", Toast.LENGTH_SHORT).show();

                }else {

                    Client client = new Client();
                    client.setName(name.getText().toString());
                    client.setPhone(phone.getText().toString());
                    client.setAddress(address.getText().toString());
                    client.setCompantName(company_name.getText().toString());
                    client.setTin(company_tin.getText().toString());
                    if(db_helper.createClient(client)){
                        // SweetAlert
                        Toast.makeText(getApplicationContext(), "Client created successful", Toast.LENGTH_SHORT).show();
                        Intent clients = new Intent(getApplicationContext(),ClientActivity.class);
                        startActivity(clients);
                        getAllProduct();

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
    public void onItemSelected(Store store) {

    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
        finish();
    }
}