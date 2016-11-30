package com.lugmity.driverapp.model;

/**
 * Model Class: Hold data for orders
 */

public class Order {

    public int id = -1;         //SQLite Order primary key - autoincrement.
    public double price = 0.0;
    public double distance = 0.0;
    public int points = 0;
    public int time = 0;
    public String ordid = null;     //order id maintained in drushti db.
    public String orderId = "";        //order id from Lugemity.
    public String restId = "";          //order's restID -- RUID
    public String title = null;
    public String google = null;
    public String name = null;
    public String email = null;
    public String mobile = null;
    public String address = null;
    public String status = null;
    public String currency = null;
    public String modified = null;
    public String created = null;
    public String loginid = null;
    public String sync = null;
}