package com.karake.EReport.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Product;
import com.karake.EReport.models.ProductType;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails extends AppCompatActivity {


    TextView name,qty,price,email;
    Product product;
    EReportDB_Helper db_helper;
    PrefManager manager;
    LinearLayout lnl_call,lnl_address,lnl_status,lnl_update;
    TextView txt_status;
    Toolbar toolbar;
    List<ProductType> productTypeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        db_helper = new EReportDB_Helper(this);
        manager = new PrefManager(getApplicationContext());
        product = db_helper.getProductByID(manager.getCurrentProduct());
        productTypeList = db_helper.getAllProductTypes();


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(product.getName());
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorOrange));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(getResources().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        

        name = findViewById(R.id.tv_name);
        qty = findViewById(R.id.tv_quantity);
        price = findViewById(R.id.tv_price);
        lnl_call = findViewById(R.id.lnl_phone_call);
        lnl_address = findViewById(R.id.lnl_address);
        lnl_status = findViewById(R.id.lnl_status);
        txt_status = findViewById(R.id.tv_status);
        lnl_update = findViewById(R.id.lnl_product_update);

        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        qty.setText(String.valueOf(product.getQuantity()));

        productStatus();

        lnl_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProductDialog();
            }
        });

    }

    private void productStatus() {
        if (product.getQuantity() >= 350){
            lnl_status.setBackground(getResources().getDrawable(R.drawable.status_navy_color));
            txt_status.setTextColor(getResources().getColor(R.color.colorNavy));
            txt_status.setText("Full Stock");
        }else if (product.getQuantity() < 350 && product.getQuantity() >= 150){
            lnl_status.setBackground(getResources().getDrawable(R.drawable.status_orange_color));
            txt_status.setTextColor(getResources().getColor(R.color.colorOrange));
            txt_status.setText("Lower Stock");
        }else if (product.getQuantity() < 150 && product.getQuantity() >= 10){
            lnl_status.setBackground(getResources().getDrawable(R.drawable.status_red_color));
            txt_status.setTextColor(getResources().getColor(R.color.colorRed));
            txt_status.setText("Critical Low Stock");
        }
    }

    private void UpdateProductDialog() {
        final AlertDialog dialog;
        final EditText name,price,qty;
        final Spinner spinnerType;
        final Button btnSave;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View viewDialog = layoutInflater.inflate(R.layout.add_new_product_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btnSave = viewDialog.findViewById(R.id.btn_save);
        name = viewDialog.findViewById(R.id.product_name);
        price = viewDialog.findViewById(R.id.product_price);
        qty = viewDialog.findViewById(R.id.product_quantity);
        spinnerType = viewDialog.findViewById(R.id.sp_product_type);

        builder.setTitle("Update Product");
        builder.setCancelable(true);
        builder.setView(viewDialog);

        List<String> names = new ArrayList<>();
        for (int i =0;i < productTypeList.size();i++){

            if(i == 0){
                names.add("Choose Product Type");
                names.add(productTypeList.get(i).getName());
            }else{
                names.add(productTypeList.get(i).getName());
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                names
        );
        // Drop down layout style - list view
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerType.setAdapter(dataAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter product name", Toast.LENGTH_SHORT).show();
                }else if (spinnerType.getSelectedItemPosition() < 1){
                    Toast.makeText(getApplicationContext(), "Please Select Product Type", Toast.LENGTH_SHORT).show();
                }else if (qty.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Product quantity", Toast.LENGTH_SHORT).show();
                }else if (price.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Price", Toast.LENGTH_SHORT).show();

                }else {

                    Product products = new Product();
                    products.setName(product.getName());
                    products.setType(spinnerType.getSelectedItem().toString());
                    products.setQuantity(product.getQuantity());
                    products.setPrice(product.getPrice());

                    if(db_helper.updateProduct(products)){
                        // SweetAlert
                        Toast.makeText(getApplicationContext(), "Product Update successful", Toast.LENGTH_SHORT).show();
                        Intent clients = new Intent(getApplicationContext(),ProductActivity.class);
                        startActivity(clients);
//                        getAllproduct();

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