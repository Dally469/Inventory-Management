package com.karake.EReport.helpers;

import android.content.Context;
import android.util.Log;

import com.karake.EReport.models.User;

public class Seeds {
    EReportDB_Helper db_helper;
    public Seeds(Context context, User user) {
        db_helper = new EReportDB_Helper(context);
        db_helper.createUser(user);
        Log.d("Creation",user.toString());
    }
}
