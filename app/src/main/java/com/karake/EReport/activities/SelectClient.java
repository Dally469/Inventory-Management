package com.karake.EReport.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karake.EReport.R;
import com.karake.EReport.adapters.ClientAdapter;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.models.Client;
import com.karake.EReport.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SelectClient extends AppCompatActivity implements ClientAdapter.ClientAdapterListener {
    EReportDB_Helper db_helper = new EReportDB_Helper(this);
    Toolbar toolbar;

    //for list Users
    RecyclerView client_recyclerView;
    ClientAdapter clientAdapter;
    List<Client> clientList = new ArrayList<>();

    FloatingActionButton fab;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_client);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //for toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select Client");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
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
                AddNewClientDialog();
            }
        });

        //List Members

        client_recyclerView = findViewById(R.id.recyclerviewProduct);
        clientAdapter = new ClientAdapter(getApplicationContext(), clientList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        client_recyclerView.setLayoutManager(mLayoutManager);
        client_recyclerView.setItemAnimator(new DefaultItemAnimator());
        client_recyclerView.setAdapter(clientAdapter);

        //get Members
        getAllClient();

    }
    private void AddNewClientDialog() {
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
                        getAllClient();

                    }else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                    }



                }

            }
        });



        dialog = builder.create();
        dialog.show();
    }

    private void getAllClient() {
        clientList.clear();
        clientList.addAll(db_helper.getAllClients());
        clientAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setFocusable(true);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setQueryHint("Search Client ...");
        searchView.requestFocusFromTouch();
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                clientAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                clientAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.setIconifiedByDefault(true);

        }else{
            Intent goHome = new Intent(SelectClient.this,MainActivity.class);
            startActivity(goHome);
            finish();
        }

    }


    @Override
    public void onClientSelected(Client client) {
        //Toast.makeText(view.getContext(), member.getId()+"", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),Sales.class);
        intent.putExtra("Name", client.getName());
        intent.putExtra("Phone", client.getPhone());
        intent.putExtra("Tin", client.getTin());
        intent.putExtra("CompanyName", client.getCompantName());
        intent.putExtra("Address", client.getAddress());
//        new PrefManager(getApplicationContext()).setCurrentClient(client.getId());
//        startActivity(intent);
        new PrefManager(this).setCurrentClient(client.getId());
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllClient();
    }
}