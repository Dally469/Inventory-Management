package com.karake.EReport.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.karake.EReport.R;
import com.karake.EReport.helpers.EReportDB_Helper;
import com.karake.EReport.helpers.Seeds;
import com.karake.EReport.models.User;
import com.karake.EReport.utils.PrefManager;

public class UserProfile extends AppCompatActivity {
    EReportDB_Helper db_helper;
    PrefManager prefManager;
    User user;
    // TextView
    TextView tv_current_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        db_helper = new EReportDB_Helper(this);
        prefManager = new PrefManager(this);
        user = new User();
        new Seeds(getApplicationContext(),user);

        tv_current_name = findViewById(R.id.currentUserName);
        tv_current_name.setText(user.getName());


    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
        finish();
    }
}