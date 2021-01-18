package com.karake.EReport.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karake.EReport.R;
import com.karake.EReport.adapters.ClientAdapter;
import com.karake.EReport.adapters.ProductAdapter;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Client;
import com.karake.EReport.models.Product;
import com.karake.EReport.models.ProductType;
import com.karake.EReport.models.Store;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.ProductAdapterListener {
    EReportDB_Helper db_helper = new EReportDB_Helper(this);
    Toolbar toolbar;

    //for list Users
    RecyclerView product_recyclerView;
    ProductAdapter productAdapter;
    List<Product> productList = new ArrayList<>();
    List<ProductType> productTypeList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        productTypeList = db_helper.getAllProductTypes();


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
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
                Intent back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(back);
                finish();
            }
        });


        //List Members
//        spinnerProductType = findViewById(R.id.sp_product_type);

        product_recyclerView = findViewById(R.id.recyclerviewProduct);
        productAdapter = new ProductAdapter(getApplicationContext(), productList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        product_recyclerView.setLayoutManager(mLayoutManager);
        product_recyclerView.setItemAnimator(new DefaultItemAnimator());
        product_recyclerView.setAdapter(productAdapter);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewProductDialog();
            }
        });
        //get Members
        getAllproduct();

    }

    private void AddNewProductDialog() {
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

        builder.setTitle("Create New Product");
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

                    Product product = new Product();
                    product.setName(name.getText().toString());
                    product.setType(spinnerType.getSelectedItem().toString());
                    product.setQuantity(Integer.parseInt(qty.getText().toString()));
                    product.setPrice(Integer.parseInt(price.getText().toString()));

                    Store store = new Store();
                    store.setProduct_name(name.getText().toString());
                    store.setProduct_quantity(Integer.parseInt(qty.getText().toString()));
                    store.setProduct_quantity_request(0);

                    if(db_helper.createProduct(product) && db_helper.createStock(store)){
                        // SweetAlert
                        Toast.makeText(getApplicationContext(), "Product created successful", Toast.LENGTH_SHORT).show();
                        Intent clients = new Intent(getApplicationContext(),ProductActivity.class);
                        startActivity(clients);
                        getAllproduct();

                    }else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        dialog = builder.create();
        dialog.show();
    }
    private void AddNewProductTypeDialog() {
        final AlertDialog dialog;
        final EditText name;
        final Button btnSave;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View viewDialog = layoutInflater.inflate(R.layout.add_new_product_type_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btnSave = viewDialog.findViewById(R.id.btn_save);
        name = viewDialog.findViewById(R.id.product_type_name);

        builder.setTitle("Create Product Type");
        builder.setCancelable(true);
        builder.setView(viewDialog);
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Product name", Toast.LENGTH_SHORT).show();
                }else {
                    ProductType productType = new ProductType();
                    productType.setName(name.getText().toString());

                    if(db_helper.createProductType(productType)){
                        Toast.makeText(getApplicationContext(), "Product Type created successful", Toast.LENGTH_SHORT).show();
                        Intent clients = new Intent(getApplicationContext(),ProductActivity.class);
                        startActivity(clients);
                        getAllproduct();
                    }else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    private void getAllproduct() {
        productList.clear();
        productList.addAll(db_helper.getAllProducts());
        productAdapter.notifyDataSetChanged();
    }

    private void emptyAllField() {

    }

    @Override
    public void onClientSelected(Product product) {
        //Toast.makeText(view.getContext(), member.getId()+"", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ProductDetails.class);
        intent.putExtra("Name", product.getName());
        intent.putExtra("Quantity", product.getQuantity());
        intent.putExtra("Price", product.getPrice());
        new PrefManager(getApplicationContext()).setCurrentProduct(product.getId());
        startActivity(intent);
    }

    // Menu option


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_product_type) {
//            Toast.makeText(this, "Selected", Toast.LENGTH_SHORT).show();
            AddNewProductTypeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllproduct();
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
        finish();
    }
}