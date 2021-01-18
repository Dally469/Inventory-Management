package com.karake.EReport.models;

public class Store {
    private int  id;
    private String  product_name;
    private int  product_quantity;
    private int  product_quantity_request;
    private String  created_at;
    private String  updated_at;

    public Store() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getProduct_quantity_request() {
        return product_quantity_request;
    }

    public void setProduct_quantity_request(int product_quantity_request) {
        this.product_quantity_request = product_quantity_request;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}

