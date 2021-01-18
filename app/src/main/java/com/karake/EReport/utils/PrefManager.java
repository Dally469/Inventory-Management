package com.karake.EReport.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "stockApp";

    private static final String IS_FIRST_TIME_LAUNCH    = "IsFirstTimeLaunch";
    private static final String IS_FIRST_TIME_LOGIN     = "IsFirstTimeLogin";
    private static final String IS_VERSION_EXPIRED      = "IsFirstTimeLogin";
    private static final String IS_SYNC                 = "IsSync";
    private static final String ID                      = "currentID";
    private static final String LAST_SYNC               = "LastSync";
    private static final String CURRENT_USER            = "currentUser";
    private static final String CURRENT_PRODUCT         = "currentProduct";
    private static final String CURRENT_CLIENT          = "currentClient";
    private static final String CURRENT_SALES           = "currentSales";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putInt(ID,0);
        editor.apply();
        editor.commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void setIsSync(Boolean isSync) {
        editor.putBoolean(IS_SYNC, isSync);
        editor.apply();
        editor.commit();
    }

    public boolean getIsSync(){
        return pref.getBoolean(IS_SYNC,true);
    }

    public void setLastSync(long time) {
        editor.putLong(LAST_SYNC, time);
        editor.apply();
        editor.commit();
    }

    public long getLastSync(){
        return pref.getLong(LAST_SYNC,0);
    }

    //current USER
    public void setCurrentUser(int id) {
        editor.putInt(CURRENT_USER, id);
        editor.apply();
        editor.commit();
    }

    public int getCurrentUser(){

        return pref.getInt(CURRENT_USER,0);
    }


    //current CLIENT
    public void setCurrentClient(int id) {
        editor.putInt(CURRENT_CLIENT, id);
        editor.apply();
        editor.commit();
    }

    public int getCurrentClient(){

        return pref.getInt(CURRENT_CLIENT,0);
    }


    //current product
    public void setCurrentProduct(int id) {
        editor.putInt(CURRENT_PRODUCT, id);
        editor.apply();
        editor.commit();
    }

    public int getCurrentProduct(){

        return pref.getInt(CURRENT_PRODUCT,0);
    }

    //current product
    public void setCurrentSales(int id) {
        editor.putInt(CURRENT_SALES, id);
        editor.apply();
        editor.commit();
    }

    public int getCurrentSales(){

        return pref.getInt(CURRENT_SALES,0);
    }
}
