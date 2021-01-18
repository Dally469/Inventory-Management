package com.karake.EReport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karake.EReport.R;
import com.karake.EReport.adapters.ProductAdapter;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Product;
import com.karake.EReport.models.ProductType;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SelectProduct extends AppCompatActivity implements ProductAdapter.ProductAdapterListener {
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
        setContentView(R.layout.activity_select_product);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        productTypeList = db_helper.getAllProductTypes();


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select Product");
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
//                AddNewProductDialog();
            }
        });
        //get Members
        getAllproduct();
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
        Intent intent = new Intent(getApplicationContext(),Sales.class);
//        intent.putExtra("Name", product.getName());
//        intent.putExtra("Quantity", product.getQuantity());
//        intent.putExtra("Price", product.getPrice());
        new PrefManager(this).setCurrentProduct(product.getId());
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }
}