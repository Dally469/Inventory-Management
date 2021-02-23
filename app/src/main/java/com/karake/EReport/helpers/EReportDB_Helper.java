package com.karake.EReport.helpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.karake.EReport.models.Client;
import com.karake.EReport.models.Product;
import com.karake.EReport.models.ProductType;
import com.karake.EReport.models.QuantityType;
import com.karake.EReport.models.Sale;
import com.karake.EReport.models.Store;
import com.karake.EReport.models.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EReportDB_Helper extends SQLiteOpenHelper{
    private static String DATABASE_NAME ="EReport.db";
    private static int DATABASE_VERSION = 1;
    public static String TABLE_ID       = "id";
    public static String CREATED_AT     = "created_at";
    public static String UPDATED_AT     = "updated_at";
    private static String DB_PATH       = "";
    Context mContext;

    public EReportDB_Helper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
    }

    public static abstract  class ER_USER implements BaseColumns{

        public static final String TABLE_ER_USER         = "ER_USER";
        public static final String COL_ER_USER_NAME      = "Name";
        public static final String COL_ER_USER_PHONE    = "phone";
        public static final String COL_ER_USER_Email      = "email";
        public static final String COL_ER_USER_PASSWORD      = "password";

    }


    public static abstract  class ER_CLIENT implements BaseColumns{

        public static final String TABLE_ER_CLIENT              = "ER_CLIENT";
        public static final String COL_ER_CLIENT_NAME           = "Name";
        public static final String COL_ER_CLIENT_PHONE          = "phone";
        public static final String COL_ER_CLIENT_COMPANY_NAME   = "CompanyName";
        public static final String COL_ER_CLIENT_TIN            = "tin";
        public static final String COL_ER_CLIENT_ADDRESS        = "address";

    }

    public static abstract  class ER_PRODUCT implements BaseColumns{

        public static final String TABLE_ER_PRODUCT         = "ER_PRODUCT";
        public static final String COL_ER_PRODUCT_NAME      = "Name";
        public static final String COL_ER_PRODUCT_TYPE      = "Type";
        public static final String COL_ER_PRODUCT_QUANTITY  = "Quantity";
        public static final String COL_ER_PRODUCT_PRICE     = "Price";

    }

    public static abstract  class ER_SALE implements BaseColumns{

        public static final String TABLE_ER_SALE                = "ER_SALE";
        public static final String COL_ER_SALE_CLIENT_ID        = "client_id";
        public static final String COL_ER_SALE_CLIENT           = "client_name";
        public static final String COL_ER_SALE_PRODUCT_ID       = "product_id";
        public static final String COL_ER_SALE_RECEIPT_ID       = "receipt_id";
        public static final String COL_ER_SALE_PRODUCT          = "product_name";
        public static final String COL_ER_SALE_QUANTITY         = "quantity";
        public static final String COL_ER_SALE_QUANTITY_TYPE    = "quantity_type";
        public static final String COL_ER_SALE_PRICE_PAID       = "price_paid";
        public static final String COL_ER_SALE_PRICE_REMAIN     = "price_remain";
        public static final String COL_ER_SALE_CURRENT_PRICE    = "current_price_id";
        public static final String COL_ER_SALE_STATUS           = "status";

    }

    public static abstract  class ER_QUANTITY implements BaseColumns{

        public static final String TABLE_ER_QUANTITY         = "ER_QUANTITY";
        public static final String COL_ER_QUANTITY_NAME      = "Name";

    }

    public static abstract  class ER_PRODUCT_TYPE implements BaseColumns{

        public static final String TABLE_ER_PRODUCT_TYPE         = "ER_PRODUCT_TYPE";
        public static final String COL_ER_PRODUCT_TYPE_NAME      = "Name";

    }

    public static abstract  class ER_STOCK implements BaseColumns{

        public static final String TABLE_ER_STOCK                         = "ER_STOCK";
        public static final String COL_ER_STOCK_PRODUCT_NAME              = "name";
        public static final String COL_ER_STOCK_PRODUCT_QUANTITY          = "quantity";
        public static final String COL_ER_STOCK_PRODUCT_QUANTITY_REQUEST  = "quantity_request";

    }

    public static final String CREATE_TABLE_ER_STOCK= "CREATE TABLE "+ER_STOCK.TABLE_ER_STOCK+
            " ("
            +TABLE_ID+" INTEGER PRIMARY KEY, "
            +ER_STOCK.COL_ER_STOCK_PRODUCT_NAME+" TEXT, "
            +ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY+" INTEGER, "
            +ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY_REQUEST+" INTEGER, "
            +CREATED_AT+" INTEGER,"
            +UPDATED_AT+" INTEGER )";

    public static final String CREATE_TABLE_ER_PRODUCT_TYPE= "CREATE TABLE "+ER_PRODUCT_TYPE.TABLE_ER_PRODUCT_TYPE+
            " ("
            +TABLE_ID+" INTEGER PRIMARY KEY, "
            +ER_PRODUCT_TYPE.COL_ER_PRODUCT_TYPE_NAME+" TEXT, "
            +CREATED_AT+" INTEGER,"
            +UPDATED_AT+" INTEGER )";

    public static final String CREATE_TABLE_ER_QUANTITY = "CREATE TABLE "+ER_QUANTITY.TABLE_ER_QUANTITY+
            " ("
            +TABLE_ID+" INTEGER PRIMARY KEY, "
            +ER_QUANTITY.COL_ER_QUANTITY_NAME+" TEXT, "
            +CREATED_AT+" INTEGER,"
            +UPDATED_AT+" INTEGER )";

    public static final String CREATE_TABLE_ER_SALE = "CREATE TABLE "+ER_SALE.TABLE_ER_SALE+
            " ("
            +TABLE_ID+" INTEGER PRIMARY KEY, "
            +ER_SALE.COL_ER_SALE_CLIENT+" TEXT, "
            +ER_SALE.COL_ER_SALE_CLIENT_ID+" INTEGER, "
            +ER_SALE.COL_ER_SALE_PRODUCT+" TEXT, "
            +ER_SALE.COL_ER_SALE_PRODUCT_ID+" INTEGER, "
            +ER_SALE.COL_ER_SALE_RECEIPT_ID+" INTEGER, "
            +ER_SALE.COL_ER_SALE_QUANTITY_TYPE+" INTEGER, "
            +ER_SALE.COL_ER_SALE_QUANTITY+" INTEGER, "
            +ER_SALE.COL_ER_SALE_PRICE_PAID+" INTEGER, "
            +ER_SALE.COL_ER_SALE_PRICE_REMAIN+" INTEGER, "
            +ER_SALE.COL_ER_SALE_CURRENT_PRICE+" INTEGER, "
            +ER_SALE.COL_ER_SALE_STATUS+" TEXT, "
            +CREATED_AT+" INTEGER,"
            +UPDATED_AT+" INTEGER )";

    public static final String CREATE_TABLE_ER_USER = "CREATE TABLE "+ER_USER.TABLE_ER_USER+
            " ("
            +TABLE_ID+" INTEGER PRIMARY KEY, "
            +ER_USER.COL_ER_USER_NAME+" TEXT, "
            +ER_USER.COL_ER_USER_PHONE+" TEXT, "
            +ER_USER.COL_ER_USER_Email+" TEXT, "
            +ER_USER.COL_ER_USER_PASSWORD+" TEXT, "
            +CREATED_AT+" INTEGER,"
            +UPDATED_AT+" INTEGER )";

    public static final String CREATE_TABLE_ER_CLIENT = "CREATE TABLE "+ER_CLIENT.TABLE_ER_CLIENT+
            " ("
            +TABLE_ID+" INTEGER PRIMARY KEY, "
            +ER_CLIENT.COL_ER_CLIENT_NAME+" TEXT, "
            +ER_CLIENT.COL_ER_CLIENT_PHONE+" TEXT, "
            +ER_CLIENT.COL_ER_CLIENT_COMPANY_NAME+" TEXT, "
            +ER_CLIENT.COL_ER_CLIENT_TIN+" TEXT, "
            +ER_CLIENT.COL_ER_CLIENT_ADDRESS+" TEXT, "
            +CREATED_AT+" INTEGER,"
            +UPDATED_AT+" INTEGER )";

    public static final String CREATE_TABLE_ER_PRODUCT = "CREATE TABLE "+ER_PRODUCT.TABLE_ER_PRODUCT+
            " ("
            +TABLE_ID+" INTEGER PRIMARY KEY, "
            +ER_PRODUCT.COL_ER_PRODUCT_NAME+" TEXT, "
            +ER_PRODUCT.COL_ER_PRODUCT_TYPE+" TEXT, "
            +ER_PRODUCT.COL_ER_PRODUCT_QUANTITY+" INTEGER, "
            +ER_PRODUCT.COL_ER_PRODUCT_PRICE+" TEXT, "
            +CREATED_AT+" INTEGER,"
            +UPDATED_AT+" INTEGER )";

    private static final String DELETE_TABLE_ER_USER            ="DROP TABLE IF EXISTS "+ ER_USER.TABLE_ER_USER;
    private static final String DELETE_TABLE_ER_CLIENT          ="DROP TABLE IF EXISTS "+ ER_CLIENT.TABLE_ER_CLIENT;
    private static final String DELETE_TABLE_ER_PRODUCT         ="DROP TABLE IF EXISTS "+ ER_PRODUCT.TABLE_ER_PRODUCT;
    private static final String DELETE_TABLE_ER_STOCK           ="DROP TABLE IF EXISTS "+ ER_STOCK.TABLE_ER_STOCK;
    private static final String DELETE_TABLE_ER_QUANTITY        ="DROP TABLE IF EXISTS "+ ER_QUANTITY.TABLE_ER_QUANTITY;
    private static final String DELETE_TABLE_ER_SALE            ="DROP TABLE IF EXISTS "+ ER_SALE.TABLE_ER_SALE;
    private static final String DELETE_TABLE_ER_PRODUCT_TYPE    ="DROP TABLE IF EXISTS "+ ER_PRODUCT_TYPE.TABLE_ER_PRODUCT_TYPE;


    // CREATING QUERIES DB

    public boolean createSale(Sale sale){
        long newLow;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_SALE.COL_ER_SALE_CLIENT_ID, sale.getClient_id());
        values.put(ER_SALE.COL_ER_SALE_PRODUCT_ID, sale.getProduct_id());
        values.put(ER_SALE.COL_ER_SALE_RECEIPT_ID, sale.getReceipt_nbr());
        values.put(ER_SALE.COL_ER_SALE_CLIENT, sale.getClient_name());
        values.put(ER_SALE.COL_ER_SALE_PRODUCT, sale.getProduct_name());
        values.put(ER_SALE.COL_ER_SALE_QUANTITY, sale.getQuantity());
        values.put(ER_SALE.COL_ER_SALE_QUANTITY_TYPE, sale.getQuantity_type());
        values.put(ER_SALE.COL_ER_SALE_PRICE_PAID, sale.getPrice_paid());
        values.put(ER_SALE.COL_ER_SALE_PRICE_REMAIN, sale.getPrice_remain());
        values.put(ER_SALE.COL_ER_SALE_CURRENT_PRICE, sale.getCurrent_price_id());
        values.put(ER_SALE.COL_ER_SALE_STATUS, sale.getStatus());
        values.put(CREATED_AT, GetUnixTime());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. insert
        newLow = db.insert(ER_SALE.TABLE_ER_SALE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        if(newLow > 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean createQuantityType(QuantityType quantityType){
        long newLow;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_QUANTITY.COL_ER_QUANTITY_NAME, quantityType.getName());
        values.put(CREATED_AT, GetUnixTime());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. insert
        newLow = db.insert(ER_QUANTITY.TABLE_ER_QUANTITY, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        if(newLow > 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean createProductType(ProductType productType){
        long newLow;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_PRODUCT_TYPE.COL_ER_PRODUCT_TYPE_NAME, productType.getName());
        values.put(CREATED_AT, GetUnixTime());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. insert
        newLow = db.insert(ER_PRODUCT_TYPE.TABLE_ER_PRODUCT_TYPE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        if(newLow > 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean createUser(User user){
        long newLow;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_USER.COL_ER_USER_NAME, user.getName());
        values.put(ER_USER.COL_ER_USER_Email, user.getEmail());
        values.put(ER_USER.COL_ER_USER_PHONE, user.getPhone());
        values.put(ER_USER.COL_ER_USER_PASSWORD, user.getPassword());
        values.put(CREATED_AT, GetUnixTime());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. insert
        newLow = db.insert(ER_USER.TABLE_ER_USER, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        if(newLow > 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean createClient(Client client){
        long newLow;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_CLIENT.COL_ER_CLIENT_NAME, client.getName());
        values.put(ER_CLIENT.COL_ER_CLIENT_PHONE, client.getPhone());
        values.put(ER_CLIENT.COL_ER_CLIENT_COMPANY_NAME, client.getCompantName());
        values.put(ER_CLIENT.COL_ER_CLIENT_TIN, client.getTin());
        values.put(ER_CLIENT.COL_ER_CLIENT_ADDRESS, client.getAddress());
        values.put(CREATED_AT, GetUnixTime());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. insert
        newLow = db.insert(ER_CLIENT.TABLE_ER_CLIENT, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        if(newLow > 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean createProduct(Product product){
        long newLow;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_PRODUCT.COL_ER_PRODUCT_NAME, product.getName());
        values.put(ER_PRODUCT.COL_ER_PRODUCT_TYPE, product.getType());
        values.put(ER_PRODUCT.COL_ER_PRODUCT_PRICE, product.getPrice());
        values.put(ER_PRODUCT.COL_ER_PRODUCT_QUANTITY, product.getQuantity());
        values.put(CREATED_AT, GetUnixTime());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. insert
        newLow = db.insert(ER_PRODUCT.TABLE_ER_PRODUCT, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        if(newLow > 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean createStock(Store store){
        long newLow;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_STOCK.COL_ER_STOCK_PRODUCT_NAME, store.getProduct_name());
        values.put(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY, store.getProduct_quantity());
        values.put(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY_REQUEST, store.getProduct_quantity_request());

        values.put(CREATED_AT, GetUnixTime());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. insert
        newLow = db.insert(ER_STOCK.TABLE_ER_STOCK, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        if(newLow > 0){
            return true;
        }else{
            return false;
        }
    }

    // UPDATING INFORMATION

    public Boolean updateProduct(Product product){

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_PRODUCT.COL_ER_PRODUCT_NAME, product.getName()); //
        values.put(ER_PRODUCT.COL_ER_PRODUCT_TYPE, product.getType()); //
        values.put(ER_PRODUCT.COL_ER_PRODUCT_PRICE, product.getPrice()); //
        values.put(ER_PRODUCT.COL_ER_PRODUCT_QUANTITY, product.getQuantity()); //
        values.put(UPDATED_AT, GetUnixTime());
        // 3. updating row
        int i = db.update(ER_PRODUCT.TABLE_ER_PRODUCT, //table
                values, // column/value
                TABLE_ID + " = ?", // selections
                new String[]{String.valueOf(product.getId())}); //selection args

        // 4. close
        db.close();

        if(i > 0){
            return true;
        }else{
            return false;
        }
    }
    public Boolean updateUser(User user){

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_USER.COL_ER_USER_NAME, user.getName()); //
        values.put(ER_USER.COL_ER_USER_Email, user.getEmail()); //
        values.put(ER_USER.COL_ER_USER_PHONE, user.getPhone()); //
        values.put(ER_USER.COL_ER_USER_PASSWORD, user.getPassword()); //
        values.put(UPDATED_AT, GetUnixTime());
        // 3. updating row
        int i = db.update(ER_USER.TABLE_ER_USER, //table
                values, // column/value
                TABLE_ID + " = ?", // selections
                new String[]{String.valueOf(user.getId())}); //selection args

        // 4. close
        db.close();

        if(i > 0){
            return true;
        }else{
            return false;
        }
    }
    public Boolean updateStock(Store store){

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_STOCK.COL_ER_STOCK_PRODUCT_NAME, store.getProduct_name()); //
        values.put(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY, store.getProduct_quantity()); //
        values.put(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY_REQUEST, store.getProduct_quantity_request()); //
        values.put(UPDATED_AT, GetUnixTime());
        // 3. updating row
        int i = db.update(ER_STOCK.TABLE_ER_STOCK, //table
                values, // column/value
                TABLE_ID + " = ?", // selections
                new String[]{String.valueOf(store.getId())}); //selection args

        // 4. close
        db.close();

        if(i > 0){
            return true;
        }else{
            return false;
        }
    }
    public Boolean updateClient(Client client){

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_CLIENT.COL_ER_CLIENT_NAME, client.getName()); //
        values.put(ER_CLIENT.COL_ER_CLIENT_ADDRESS, client.getAddress()); //
        values.put(ER_CLIENT.COL_ER_CLIENT_PHONE, client.getPhone()); //
        values.put(ER_CLIENT.COL_ER_CLIENT_COMPANY_NAME, client.getCompantName()); //
        values.put(ER_CLIENT.COL_ER_CLIENT_TIN, client.getTin()); //
        values.put(UPDATED_AT, GetUnixTime());
        // 3. updating row
        int i = db.update(ER_PRODUCT.TABLE_ER_PRODUCT, //table
                values, // column/value
                TABLE_ID + " = ?", // selections
                new String[]{String.valueOf(client.getId())}); //selection args

        // 4. close
        db.close();

        if(i > 0){
            return true;
        }else{
            return false;
        }
    }
    public Boolean updateSale(Sale sale){

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(ER_SALE.COL_ER_SALE_CLIENT_ID, sale.getClient_id());
        values.put(ER_SALE.COL_ER_SALE_PRODUCT_ID, sale.getProduct_id());
        values.put(ER_SALE.COL_ER_SALE_RECEIPT_ID, sale.getReceipt_nbr());
        values.put(ER_SALE.COL_ER_SALE_CLIENT, sale.getClient_name());
        values.put(ER_SALE.COL_ER_SALE_PRODUCT, sale.getProduct_name());
        values.put(ER_SALE.COL_ER_SALE_QUANTITY, sale.getQuantity());
        values.put(ER_SALE.COL_ER_SALE_QUANTITY_TYPE, sale.getQuantity_type());
        values.put(ER_SALE.COL_ER_SALE_PRICE_PAID, sale.getPrice_paid());
        values.put(ER_SALE.COL_ER_SALE_PRICE_REMAIN, sale.getPrice_remain());
        values.put(ER_SALE.COL_ER_SALE_CURRENT_PRICE, sale.getCurrent_price_id());
        values.put(ER_SALE.COL_ER_SALE_STATUS, sale.getStatus());
        values.put(UPDATED_AT, GetUnixTime());
        // 3. updating row
        int i = db.update(ER_SALE.TABLE_ER_SALE, //table
                values, // column/value
                TABLE_ID + " = ?", // selections
                new String[]{String.valueOf(sale.getId())}); //selection args

        // 4. close
        db.close();

        if(i > 0){
            return true;
        }else{
            return false;
        }
    }

    // GETTING INFORMATION DB

    public Product getProductByID(int id){

        // 1. build the query
        String query = "SELECT  * FROM " + ER_PRODUCT.TABLE_ER_PRODUCT+ " WHERE "+TABLE_ID+" = ?";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        // 3. go over each row, build event and add it to list
        Product product = null;
        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                product.setPrice(cursor.getInt(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_PRICE)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_QUANTITY)));
                product.setName(cursor.getString(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_NAME)));
                product.setType(cursor.getString(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_TYPE)));
                product.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                product.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));

            }while (cursor.moveToNext());
        }

        return product;
    }
    public Store getStoreByID(int id){

        // 1. build the query
        String query = "SELECT  * FROM " + ER_STOCK.TABLE_ER_STOCK+ " WHERE "+TABLE_ID+" = ?";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        // 3. go over each row, build event and add it to list
        Store store = null;
        if (cursor.moveToFirst()) {
            do {
                store = new Store();
                store.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                store.setProduct_name(cursor.getString(cursor.getColumnIndex(ER_STOCK.COL_ER_STOCK_PRODUCT_NAME)));
                store.setProduct_quantity(cursor.getInt(cursor.getColumnIndex(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY)));
                store.setProduct_quantity_request(cursor.getInt(cursor.getColumnIndex(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY_REQUEST)));
                store.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                store.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));

            }while (cursor.moveToNext());
        }

        return store;
    }
    public User getUserByCredential(String phone,String password){

        // 1. build the query
        String query = "SELECT  * FROM " + ER_USER.TABLE_ER_USER+ " WHERE "+ER_USER.COL_ER_USER_PHONE+" = ? AND "+ER_USER.COL_ER_USER_PASSWORD+" = ?";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{phone,password});

        // 3. go over each row, build event and add it to list
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(ER_USER.COL_ER_USER_Email)));
                user.setName(cursor.getString(cursor.getColumnIndex(ER_USER.COL_ER_USER_NAME)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(ER_USER.COL_ER_USER_PHONE)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(ER_USER.COL_ER_USER_PASSWORD)));
                user.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                user.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                //Add member to members
            }while (cursor.moveToNext());
        }

        //Log.d("getMemberById()", member.toString());
        return user;
    }
    public Client getClientByID(int id){

        // 1. build the query
        String query = "SELECT  * FROM " + ER_CLIENT.TABLE_ER_CLIENT+ " WHERE "+TABLE_ID+" = ?";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        // 3. go over each row, build event and add it to list
        Client client = null;
        if (cursor.moveToFirst()) {
            do {
                client = new Client();
                client.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                client.setCompantName(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_COMPANY_NAME)));
                client.setTin(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_TIN)));
                client.setName(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_NAME)));
                client.setPhone(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_PHONE)));
                client.setAddress(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_ADDRESS)));
                client.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                client.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));

            }while (cursor.moveToNext());
        }

        //Log.d("getMemberById()", member.toString());
        return client;
    }

    public List<Store> getAllStock(){

        List<Store> stores = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + ER_STOCK.TABLE_ER_STOCK+" " +"ORDER BY "+ER_STOCK.TABLE_ER_STOCK+"."+CREATED_AT+" DESC";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build event and add it to list
        Store store = null;
        if (cursor.moveToFirst()) {

            do {
                store = new Store();
                store.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                store.setProduct_name(cursor.getString(cursor.getColumnIndex(ER_STOCK.COL_ER_STOCK_PRODUCT_NAME)));
                store.setProduct_quantity(cursor.getInt(cursor.getColumnIndex(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY)));
                store.setProduct_quantity_request(cursor.getInt(cursor.getColumnIndex(ER_STOCK.COL_ER_STOCK_PRODUCT_QUANTITY_REQUEST)));
                store.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                store.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Add sale to sales
                stores.add(store);
            } while (cursor.moveToNext());
        }
        // return quantityTypes
        return stores;
    }
    public List<Sale> getAllSales(){

        List<Sale> sales = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + ER_SALE.TABLE_ER_SALE+" " +"ORDER BY "+ER_SALE.TABLE_ER_SALE+"."+CREATED_AT+" DESC";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build event and add it to list
        Sale sale = null;
        if (cursor.moveToFirst()) {

            do {
                sale = new Sale();
                sale.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                sale.setClient_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CLIENT_ID)));
                sale.setProduct_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRODUCT_ID)));
                sale.setReceipt_nbr(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_RECEIPT_ID)));
                sale.setClient_name(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CLIENT)));
                sale.setProduct_name(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRODUCT)));
                sale.setQuantity(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_QUANTITY)));
                sale.setQuantity_type(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_QUANTITY_TYPE)));
                sale.setPrice_paid(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRICE_PAID)));
                sale.setPrice_remain(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRICE_REMAIN)));
                sale.setCurrent_price_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CURRENT_PRICE)));
                sale.setStatus(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_STATUS)));
                sale.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                sale.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Add sale to sales
                sales.add(sale);
            } while (cursor.moveToNext());
        }
        // return quantityTypes
        return sales;
    }
    public List<Sale> getAllSalesByClientID(int clientID){

        List<Sale> sales = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + ER_SALE.TABLE_ER_SALE+ " WHERE "+ER_SALE.COL_ER_SALE_CLIENT_ID+" = ?";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(clientID)});

        Sale sale = null;
        if (cursor.moveToFirst()) {

            do {
                sale = new Sale();
                sale.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                sale.setClient_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CLIENT_ID)));
                sale.setProduct_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRODUCT_ID)));
                sale.setReceipt_nbr(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_RECEIPT_ID)));
                sale.setClient_name(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CLIENT)));
                sale.setProduct_name(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRODUCT)));
                sale.setQuantity(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_QUANTITY)));
                sale.setQuantity_type(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_QUANTITY_TYPE)));
                sale.setPrice_paid(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRICE_PAID)));
                sale.setPrice_remain(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRICE_REMAIN)));
                sale.setCurrent_price_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CURRENT_PRICE)));
                sale.setStatus(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_STATUS)));
                sale.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                sale.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Add sale to sales
                sales.add(sale);
            } while (cursor.moveToNext());
        }
        // return quantityTypes
        return sales;
    }
    public  Sale  getSaleByID(int saleID){

//        List<Sale> sales = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + ER_SALE.TABLE_ER_SALE+ " WHERE "+TABLE_ID+" = ?";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(saleID)});

        // 3. go over each row, build event and add it to list
        Sale sale = null;
        if (cursor.moveToFirst()) {
            do {
                sale = new Sale();
                sale.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                sale.setClient_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CLIENT_ID)));
                sale.setProduct_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRODUCT_ID)));
                sale.setReceipt_nbr(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_RECEIPT_ID)));
                sale.setClient_name(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CLIENT)));
                sale.setProduct_name(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRODUCT)));
                sale.setQuantity(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_QUANTITY)));
                sale.setQuantity_type(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_QUANTITY_TYPE)));
                sale.setPrice_paid(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRICE_PAID)));
                sale.setPrice_remain(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_PRICE_REMAIN)));
                sale.setCurrent_price_id(cursor.getInt(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_CURRENT_PRICE)));
                sale.setStatus(cursor.getString(cursor.getColumnIndex(ER_SALE.COL_ER_SALE_STATUS)));
                sale.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                sale.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));

            }while (cursor.moveToNext());
        }

        //Log.d("getMemberById()", member.toString());
        return sale;

    }
    public List<QuantityType> getAllQuantityTypes(){

        List<QuantityType> quantityTypes = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + ER_QUANTITY.TABLE_ER_QUANTITY;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build event and add it to list
        QuantityType quantityType = null;
        if (cursor.moveToFirst()) {

            do {
                quantityType = new QuantityType();
                quantityType.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                quantityType.setName(cursor.getString(cursor.getColumnIndex(ER_QUANTITY.COL_ER_QUANTITY_NAME)));
                quantityType.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                quantityType.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Add client to clients
                quantityTypes.add(quantityType);
            } while (cursor.moveToNext());
        }
        // return quantityTypes
        return quantityTypes;
    }
    public List<ProductType> getAllProductTypes(){

        List<ProductType> productTypes = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + ER_PRODUCT_TYPE.TABLE_ER_PRODUCT_TYPE+" " +"ORDER BY "+ER_PRODUCT_TYPE.TABLE_ER_PRODUCT_TYPE+"."+CREATED_AT+" DESC";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build event and add it to list
        ProductType productType = null;
        if (cursor.moveToFirst()) {

            do {
                productType = new ProductType();
                productType.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                productType.setName(cursor.getString(cursor.getColumnIndex(ER_PRODUCT_TYPE.COL_ER_PRODUCT_TYPE_NAME)));
                productType.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                productType.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Add client to clients
                productTypes.add(productType);
            } while (cursor.moveToNext());
        }
        // return quantityTypes
        return productTypes;
    }
    public List<Client> getAllClients(){
        List<Client> clients = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + ER_CLIENT.TABLE_ER_CLIENT+" " +"ORDER BY "+ER_CLIENT.TABLE_ER_CLIENT+"."+CREATED_AT+" DESC";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build event and add it to list
        Client client = null;
        if (cursor.moveToFirst()) {

            do {
                client = new Client();
                client.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                client.setName(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_NAME)));
                client.setPhone(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_PHONE)));
                client.setCompantName(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_COMPANY_NAME)));
                client.setTin(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_TIN)));
                client.setAddress(cursor.getString(cursor.getColumnIndex(ER_CLIENT.COL_ER_CLIENT_ADDRESS)));
                client.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                client.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Add client to clients
                clients.add(client);
            } while (cursor.moveToNext());
        }
        // return clients
        return clients;
    }
    public List<Product> getAllProducts(){
        List<Product> products = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + ER_PRODUCT.TABLE_ER_PRODUCT+" " +"ORDER BY "+ER_PRODUCT.TABLE_ER_PRODUCT+"."+CREATED_AT+" DESC";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build event and add it to list
        Product product = null;
        if (cursor.moveToFirst()) {

            do {
                product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ID)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_QUANTITY)));
                product.setName(cursor.getString(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_NAME)));
                product.setType(cursor.getString(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_TYPE)));
                product.setPrice(cursor.getInt(cursor.getColumnIndex(ER_PRODUCT.COL_ER_PRODUCT_PRICE)));
                product.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                product.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Add product to products
                products.add(product);
            } while (cursor.moveToNext());
        }
        // return groupTypes
        return products;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_ER_USER);
        db.execSQL(CREATE_TABLE_ER_CLIENT);
        db.execSQL(CREATE_TABLE_ER_PRODUCT);
        db.execSQL(CREATE_TABLE_ER_QUANTITY);
        db.execSQL(CREATE_TABLE_ER_SALE);
        db.execSQL(CREATE_TABLE_ER_PRODUCT_TYPE);
        db.execSQL(CREATE_TABLE_ER_STOCK);

    }

    public void empty_AllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ER_SALE.TABLE_ER_SALE, null, null);
        db.delete(ER_QUANTITY.TABLE_ER_QUANTITY, null, null);
        db.delete(ER_PRODUCT.TABLE_ER_PRODUCT, null, null);
        db.delete(ER_CLIENT.TABLE_ER_CLIENT, null, null);
        db.delete(ER_USER.TABLE_ER_USER, null, null);
        db.delete(ER_PRODUCT_TYPE.TABLE_ER_PRODUCT_TYPE, null, null);
        db.delete(ER_STOCK.TABLE_ER_STOCK, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DELETE_TABLE_ER_SALE);
        db.execSQL(DELETE_TABLE_ER_STOCK);
        db.execSQL(DELETE_TABLE_ER_QUANTITY);
        db.execSQL(DELETE_TABLE_ER_PRODUCT_TYPE);
        db.execSQL(DELETE_TABLE_ER_PRODUCT);
        db.execSQL(DELETE_TABLE_ER_CLIENT);
        db.execSQL(DELETE_TABLE_ER_USER);

        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA FOREIGN_KEYS=ON ");
    }
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
        } else {
            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database "+e.toString());

            }
        }

    }
    private boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException
    {
        // Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
    public int GetUnixTime()
    {
        int utc  = (int)(new Date().getTime()/1000);
        return (utc);

    }
}

