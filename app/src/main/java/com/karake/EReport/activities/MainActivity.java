package com.karake.EReport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Sale;
import com.karake.EReport.models.User;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CardView lnl_client,lnl_purchased,lnl_store,lnl_product;
    LinearLayout lnl_profile;
    PrefManager prefManager;
    TextView tv_balance,tv_remain,currentUser;
    User user;
    private List<Sale> saleList = new ArrayList<>();
    EReportDB_Helper db_helper;
    int total_balance = 0;
    int total_remain = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        prefManager = new PrefManager(getApplicationContext());
        db_helper = new EReportDB_Helper(this);
        user = new User();

        if(prefManager.getCurrentUser() < 1){
            Intent goToLogin = new Intent(this,LoginActivity.class);
            startActivity(goToLogin);
            finish();
        }

        saleList = db_helper.getAllSales();

        for (Sale sale:saleList)
            total_balance+= sale.getPrice_paid();
        for (Sale sales:saleList)
            total_remain+= sales.getPrice_remain();

        // prepare initialization
        lnl_client = findViewById(R.id.lnl_client);
        lnl_purchased = findViewById(R.id.lnl_sales);
        lnl_product = findViewById(R.id.lnl_product);
        lnl_store = findViewById(R.id.lnl_other);
        lnl_profile = findViewById(R.id.lnl_profile);
        tv_balance = findViewById(R.id.total_balance);
        tv_remain = findViewById(R.id.total_remain);


        //Set vales
        tv_balance.setText(String.valueOf(total_balance));
        tv_remain.setText(String.valueOf(total_remain));


        lnl_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent client = new Intent(getApplicationContext(),ClientActivity.class);
                startActivity(client);

            }
        });
        lnl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(getApplicationContext(),UserProfile.class);
                startActivity(user);

            }
        });
        lnl_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stock = new Intent(getApplicationContext(),ProductActivity.class);
                startActivity(stock);

            }
        });

        lnl_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sales = new Intent(getApplicationContext(),Sales.class);
                startActivity(sales);

            }
        });

        lnl_purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stock = new Intent(getApplicationContext(),Stock.class);
                startActivity(stock);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_balance.setText(String.valueOf(total_balance));
        tv_remain.setText(String.valueOf(total_remain));
    }
}