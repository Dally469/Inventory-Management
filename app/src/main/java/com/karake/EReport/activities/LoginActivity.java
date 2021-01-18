package com.karake.EReport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.helpers.Seeds;
import com.karake.EReport.models.User;
import com.karake.EReport.utils.PrefManager;

public class LoginActivity extends AppCompatActivity {

    EditText ed_telephone;
    EditText ed_password;
    EReportDB_Helper db_helper;
    User current_user,seed_user;
    Button btn_login;
    PrefManager prefManager;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        prefManager = new PrefManager(getApplicationContext());
        db_helper = new EReportDB_Helper(getApplicationContext());
        progressDialog = new ProgressDialog(this);


        seed_user = new User();
        seed_user.setPassword("12345678");
        seed_user.setPhone("0785194263");
        seed_user.setEmail("dallyjones@gmail.com");
        seed_user.setName("Dally Jones Dev");
        new Seeds(getApplicationContext(),seed_user);

        if(prefManager.getCurrentUser() > 1){
            Intent gotToMain = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(gotToMain);
            finish();
        }

        ed_telephone = findViewById(R.id.ed_telephone);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ed_telephone.getText().toString().isEmpty() || ed_password.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(), "Enter Telephone & Password", Toast.LENGTH_SHORT).show();

                }else{
                    //Toast.makeText(LoginActivity.this, ed_telephone.getText().toString()+" / "+ed_password.getText().toString(), Toast.LENGTH_SHORT).show();
                    current_user = db_helper.getUserByCredential(ed_telephone.getText().toString(),ed_password.getText().toString());

                    if(current_user != null){
                        Toast.makeText(getApplicationContext(), "Cool! User Found.", Toast.LENGTH_SHORT).show();
                        new PrefManager(getApplicationContext()).setCurrentUser(current_user.getId());
                        Intent intent  = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();


                    }else{
                        Toast.makeText(getApplicationContext(), "User Not Found!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
}