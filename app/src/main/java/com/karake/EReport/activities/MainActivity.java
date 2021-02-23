package com.karake.EReport.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Sale;
import com.karake.EReport.models.User;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CardView lnl_client,lnl_purchased,lnl_store,lnl_product;
    LinearLayout lnl_sync;
    PrefManager prefManager;
    TextView tv_balance,tv_remain,currentUser;
    User user;
    private List<Sale> saleList = new ArrayList<>();
    EReportDB_Helper db_helper;
    int total_balance = 0;
    int total_remain = 0;
    TextView currentUser_name;
    ImageView img_sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        prefManager = new PrefManager(getApplicationContext());
        db_helper = new EReportDB_Helper(this);
        user = new PrefManager(getApplicationContext()).getLoggedUser();

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
        img_sync = findViewById(R.id.data_sync);
        lnl_client = findViewById(R.id.lnl_client);
        lnl_purchased = findViewById(R.id.lnl_sales);
        lnl_product = findViewById(R.id.lnl_product);
        lnl_store = findViewById(R.id.lnl_other);
        lnl_sync = findViewById(R.id.lnl_profile);
        tv_balance = findViewById(R.id.total_balance);
        tv_remain = findViewById(R.id.total_remain);
        currentUser_name = findViewById(R.id.currentUser_Name);
        
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

        if (!isNetworkAvailable(getApplicationContext())){
            Toast.makeText(this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
            img_sync.setImageDrawable(getResources().getDrawable(R.drawable.not_synced));
            lnl_sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent user = new Intent(getApplicationContext(),UserProfile.class);
                    startActivity(user);

                }
            });
        }else {
            Toast.makeText(this, "Now You're Connected", Toast.LENGTH_SHORT).show();

            img_sync.setImageDrawable(getResources().getDrawable(R.drawable.synced));
        }


    }

    public static boolean isNetworkAvailable(Context context) {

        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                isConnected =  true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                isConnected =  true;
            }

        } else {

            isConnected = false;
        }
        return  isConnected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_balance.setText(String.valueOf(total_balance));
        tv_remain.setText(String.valueOf(total_remain));
    }
}