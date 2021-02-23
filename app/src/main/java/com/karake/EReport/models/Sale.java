package com.karake.EReport.models;

import android.text.format.DateFormat;

public class Sale {
    private int  id;
    private int  client_id;
    private int  product_id;
    private int  receipt_nbr;
    private String  client_name;
    private String  product_name;
    private int  quantity;
    private int  quantity_type;
    private int  price_paid;
    private int  price_remain;
    private int  current_price_id;
    private String  status;
    private String  created_at;
    private String  updated_at;

    public Sale() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getReceipt_nbr() {
        return receipt_nbr;
    }

    public void setReceipt_nbr(int receipt_nbr) {
        this.receipt_nbr = receipt_nbr;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity_type() {
        return quantity_type;
    }

    public void setQuantity_type(int quantity_type) {
        this.quantity_type = quantity_type;
    }

    public int getPrice_paid() {
        return price_paid;
    }

    public void setPrice_paid(int price_paid) {
        this.price_paid = price_paid;
    }

    public int getPrice_remain() {
        return price_remain;
    }

    public void setPrice_remain(int price_remain) {
        this.price_remain = price_remain;
    }

    public int getCurrent_price_id() {
        return current_price_id;
    }

    public void setCurrent_price_id(int current_price_id) {
        this.current_price_id = current_price_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        String dateReal = DateFormat.format("dd-MM-yyyy h:m", Long.parseLong(created_at)* 1000L).toString();

        return dateReal;

    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        String dateReal = DateFormat.format("dd-MM-yyyy h:m", Long.parseLong(updated_at)* 1000L).toString();

        return dateReal;

    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
