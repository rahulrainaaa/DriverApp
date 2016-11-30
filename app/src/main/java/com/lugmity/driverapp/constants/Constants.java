package com.lugmity.driverapp.constants;

import com.google.android.gms.maps.model.LatLng;
import com.lugmity.driverapp.model.FreelanceUserData;
import com.lugmity.driverapp.model.Order;
import com.lugmity.driverapp.model.Restaurant;
import com.lugmity.driverapp.model.SponsoredUserData;

import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * @class Constants
 * @desc Constant class for holding all static values at runtime.
 */
public class Constants {

    //Configurations
    public static String strUserType = "freelance";         /*Holds the user-type in memory*/
    public static String strLanguage = "english";           /*Holds the locale for the application*/
    public static String LOGIN_ID = "-1";
    public static int LOGIN_FLAG = 0;

    //Map LatLong points
    public static LatLng POINT_START = null;
    public static LatLng POINT_DEST = null;                 /*Destination LatLng to be pointed on map*/

    //Cache file names
    public static String CACHE_DATA = "data";               /*cache file name*/

    //Login user data holding model class objects.
    public static FreelanceUserData freelanceUserData = null;   /*freelance model class object*/
    public static SponsoredUserData sponsoredUserData = null;   /*sponsored model class object*/

    //flag for localization handling
    public static int start = 0;

    //Regular expression Patterns for validations
    public static Pattern patternMobile = Pattern.compile("^(\\d+)$");                                          //mobile number
    public static Pattern patternCountryCode = Pattern.compile("^[+](\\d+)$");                                  //country code
    public static Pattern patternFullName = Pattern.compile("^[A-Za-z\\s]+$");                                  //full name
    public static Pattern patternUsernamePassword = Pattern.compile("^[A-Za-z].([A-Za-z]|[0-9]|_)+$");          //username, password
    public static Pattern patternEmail = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");         //email

    //SQLite Database(s)
    public static String DB = "sponsor_orders";

    //SQLite Queries
    public static String SQL_QUERY = "SELECT * FROM orders";
    public static String SQL_CREATE = "CREATE TABLE IF NOT EXISTS orders(id INTEGER PRIMARY KEY NOT NULL, ordid VARCHAR, orderno VARCHAR, ordername VARCHAR, customer VARCHAR, address VARCHAR, geo VARCHAR, mob VARCHAR, email VARCHAR, distance VARCHAR, price VARCHAR, points VARCHAR, cur VARCHAR, status VARCHAR, sync VARCHAR);";
    public static String SQL_INSERT_ORDER = "Insert into orders (ordid, orderno, ordername, customer, address, geo, mob, email, distance, price, points, cur, status, sync) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    //Freelance driver selected restaurant.
    public static Restaurant restaurant = null;

    //Customer Order selected.
    public static Order order = null;

    //Temp JSONObject - HTTP Request Payload - for carrying data to next Activity.
    public static JSONObject httpJsonRequest = null;
}