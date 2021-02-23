package com.karake.EReport.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.karake.EReport.adapters.ProductAdapter;
import com.karake.EReport.adapters.SalesAdapter;
import com.karake.EReport.adapters.SingleClientSalesAdapter;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Client;
import com.karake.EReport.models.Product;
import com.karake.EReport.models.ProductType;
import com.karake.EReport.models.Sale;
import com.karake.EReport.models.Store;
import com.karake.EReport.prints.BluetoothService;
import com.karake.EReport.prints.Command;
import com.karake.EReport.prints.DeviceListActivity;
import com.karake.EReport.prints.PrintPicture;
import com.karake.EReport.prints.PrinterCommand;
import com.karake.EReport.utils.PrefManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Sales extends AppCompatActivity implements SingleClientSalesAdapter.SalesAdapterListener{
    /******************************************************************************************************/
    // Debugging
    private static final String TAG = "Sales_Activity";
    private static final boolean DEBUG = true;
    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;
    private static final int REQUEST_CAMERA = 4;

    //QRcode
    private static final int QR_WIDTH = 350;
    private static final int QR_HEIGHT = 350;
    /*******************************************************************************************************/
    private static final String CHINESE = "GBK";

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mService = null;

    View view;

    private Button pickDate,scanQR,saveDonation;
    private String currentDateString;
    private EditText tv_member_code,tv_member_amount;
    Calendar calender = Calendar.getInstance();
    public Sales() {
        // Required empty public constructor
    }

    boolean isPrintChecked = false;
    int isNotifyChecked = 0;

    EReportDB_Helper db_helper = new EReportDB_Helper(this);
    Toolbar toolbar;

    //for list Users
    RecyclerView sales_recyclerView;
    SingleClientSalesAdapter salesAdapter;
    List<Sale> salesList = new ArrayList<>();
    PrefManager manager;
    Client client = new Client();
    Product product = new Product();
    Store stores = new Store();
    String selectedClientID,selectedProductID;
    ImageView logo;
    FloatingActionButton fab;
    LinearLayout lnl_list;

    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //new PrefManager(getApplicationContext()).setCurrentClient(0);
//        new PrefManager(getApplicationContext()).setCurrentProduct(0);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sales");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorGray));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sales_recyclerView = findViewById(R.id.recyclerviewSales);
        salesAdapter = new SingleClientSalesAdapter(getApplicationContext(), salesList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sales_recyclerView.setLayoutManager(mLayoutManager);
        sales_recyclerView.setItemAnimator(new DefaultItemAnimator());
        sales_recyclerView.setAdapter(salesAdapter);

        lnl_list = findViewById(R.id.lnl_list_of_sales_dispatch);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewSalesDialog();
            }
        });
        //get Sales
        getAllSales();

    }

    private void AddNewSalesDialog() {
        final AlertDialog dialog;
        final EditText ed_client_id,ed_product_id,ed_quantity,ed_price,ed_paid,ed_invoice;
        final Button btnSave;
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        final View viewDialog = layoutInflater.inflate(R.layout.add_new_sales_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btnSave = viewDialog.findViewById(R.id.btn_save);
        ed_client_id = viewDialog.findViewById(R.id.client_company_name);
        ed_product_id = viewDialog.findViewById(R.id.product_name);
        ed_quantity = viewDialog.findViewById(R.id.product_quantity);
        ed_price = viewDialog.findViewById(R.id.product_price_total);
        ed_paid = viewDialog.findViewById(R.id.product_price_paid);
        ed_invoice = viewDialog.findViewById(R.id.product_price_remain);

        if(new PrefManager(getApplicationContext()).getCurrentClient() > 0){

            client  = db_helper.getClientByID(new PrefManager(getApplicationContext()).getCurrentClient());
            ed_client_id.setText(client.getCompantName());

            selectedClientID= String.valueOf(new PrefManager(getApplicationContext()).getCurrentClient());
        }
        if(new PrefManager(getApplicationContext()).getCurrentProduct() > 0){

            product  = db_helper.getProductByID(new PrefManager(getApplicationContext()).getCurrentProduct());
            ed_product_id.setText(product.getName());
            ed_price.setText(String.valueOf(product.getPrice()));

            selectedProductID= String.valueOf(new PrefManager(getApplicationContext()).getCurrentProduct());
        }
        ed_client_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoClient = new Intent(getApplicationContext(), SelectClient.class);
                startActivity(gotoClient);
            }
        });
        ed_product_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoProduct = new Intent(getApplicationContext(), SelectProduct.class);
                startActivity(gotoProduct);
            }
        });



        builder.setTitle("Create new sales");
        builder.setCancelable(true);
        builder.setView(viewDialog);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_client_id.getText().toString().isEmpty()){
                    ed_client_id.setError("Please enter Client");
                    ed_client_id.requestFocus();
                }else  if (ed_invoice.getText().toString().isEmpty()){
                    ed_invoice.setError("Please enter Receipt Number");
                    ed_invoice.requestFocus();
                }else if (ed_product_id.getText().toString().isEmpty()){
                    ed_product_id.setError("Please Enter Product");
                    ed_product_id.requestFocus();
                }else if (ed_quantity.getText().toString().isEmpty()){
                    ed_quantity.setError("Please Enter quantity");
                    ed_quantity.requestFocus();
                }else if (product.getQuantity() < Integer.parseInt(ed_quantity.getText().toString().trim())){
                    ed_quantity.setError("Please Enter Available quantity");
                    ed_quantity.requestFocus();
                }else if (ed_price.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Price", Toast.LENGTH_SHORT).show();
                }else if (ed_paid.getText().toString().isEmpty()){
                    ed_paid.setError("Please Enter Paid amount");
                    ed_paid.requestFocus();
                }else {
                    Sale sale = new Sale();
                    int total = 0;
                    int balance = 0;
                    sale.setClient_id(client.getId());
                    sale.setClient_name(ed_client_id.getText().toString());
                    sale.setProduct_id(product.getId());
                    sale.setReceipt_nbr(Integer.parseInt(ed_invoice.getText().toString()));
                    sale.setProduct_name(ed_product_id.getText().toString());
                    sale.setQuantity(Integer.parseInt(ed_quantity.getText().toString()));
                    total+=(Integer.parseInt(ed_quantity.getText().toString()))*(Integer.parseInt(ed_price.getText().toString()));
                    sale.setCurrent_price_id(total);
                    balance+=(total - Integer.parseInt(ed_paid.getText().toString()));
                    sale.setPrice_paid(Integer.parseInt(ed_paid.getText().toString()));
                    sale.setPrice_remain(balance);
                    sale.setStatus("0");

                    if(db_helper.createSale(sale)){
                        // SweetAlert
                        Toast.makeText(getApplicationContext(), "Product Sold Successfully", Toast.LENGTH_SHORT).show();
                        Intent sold = new Intent(getApplicationContext(),Sales.class);
                        // Stock Decreament according to sale out
                        Product product1 = db_helper.getProductByID(product.getId());
                        product1.setQuantity(product.getQuantity()  - Integer.parseInt(ed_quantity.getText().toString()));
                        db_helper.updateProduct(product1);

//                        Store store = db_helper.getStoreByID(stores.getId());
//                        store.setProduct_quantity(stores.getProduct_quantity() - Integer.parseInt(ed_quantity.getText().toString()));
//                        store.setProduct_quantity_request(stores.getProduct_quantity() + Integer.parseInt(ed_quantity.getText().toString()));
//                        db_helper.updateStock(store);

                        startActivity(sold);
                        getAllSales();
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


    private void getAllSales() {
        salesList.clear();
        salesList.addAll(db_helper.getAllSales());
        salesAdapter.notifyDataSetChanged();
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
        tv_quantity.setText(sale.getQuantity() + " Items");
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
                        getAllSales();
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


    @Override
    public synchronized void onResume() {
        super.onResume();
        getAllSales();

    }


}