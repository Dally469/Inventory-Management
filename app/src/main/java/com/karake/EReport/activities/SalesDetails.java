package com.karake.EReport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Sale;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class SalesDetails extends AppCompatActivity {
    EReportDB_Helper db_helper;
    PrefManager prefManager;
    Sale sale;
    Toolbar toolbar;

    TextView tv_company,tv_customer,tv_product,tv_quantity,tv_price,tv_total_price,tv_total_paid,tv_total_remain,tv_issue_at,tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_details);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        db_helper = new EReportDB_Helper(getApplicationContext());
        prefManager = new PrefManager(getApplicationContext());
        sale = db_helper.getSaleByID(prefManager.getCurrentSales());
        // toolbar info
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(sale.getClient_name());
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

        tv_company = findViewById(R.id.sl_company_name);
        tv_customer = findViewById(R.id.sl_customer_name);
        tv_product = findViewById(R.id.sl_product_name);
        tv_quantity = findViewById(R.id.sl_product_qty);
        tv_total_paid = findViewById(R.id.sl_product_tot_paid);
        tv_total_price = findViewById(R.id.sl_product_tot_amount);
        tv_total_remain = findViewById(R.id.sl_product_tot_remain);
        tv_status = findViewById(R.id.sl_status);
        tv_issue_at = findViewById(R.id.sl_issue_at);

        tv_company.setText(sale.getClient_name());
        tv_customer.setText(sale.getClient_name());
        tv_product.setText(sale.getProduct_name());
        tv_quantity.setText(String.valueOf(sale.getQuantity() + " Item"));
        tv_total_paid.setText(String.valueOf(sale.getPrice_paid()+ " Frw"));
        tv_total_price.setText(String.valueOf(sale.getCurrent_price_id()+ " Frw"));
        tv_total_remain.setText(String.valueOf(sale.getPrice_remain()+ " Frw"));
        tv_status.setText(sale.getStatus());
        tv_issue_at.setText(sale.getCreated_at());
    }
}