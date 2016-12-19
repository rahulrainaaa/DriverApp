package com.lugmity.driverapp.network;

/**
 * Network-Constant: Class for holding HTTP Data and URL.
 */
public class Network {
    /**
     drivers.lugmety.com
     lugmety.drushtiindia.com
     */

    //--------------Drushti Provided URL(s)-----------------------------------------------

    public static String AUTH_LOGIN = "lugmetyKeys%20";
    public static String URL_LOGIN = "http://drivers.lugmety.com/Apicalls/userlogin/";

    public static String AUTH_REGISTER = "lugmetyKeys%221";
    public static String URL_REGISTER = "http://drivers.lugmety.com/Apicalls/addFDriver/";

    public static String AUTH_CHANGE_PASS = "lugmetyKeys%211";
    public static String URL_CHANGE_PASS = "http://drivers.lugmety.com/Apicalls/changepassword/";

    public static String URL_GET_IMG = "http://drivers.lugmety.com/Apicalls/freelanceprofile/";

    public static String URL_GET_ORDERS = "http://drivers.lugmety.com/Apicalls/getorderdetails/";

    public static String URL_ADD_ORDER = "http://drivers.lugmety.com/Apicalls/addorederdetails/";

    public static String URL_GET_ALL_REST = "http://drivers.lugmety.com/Apicalls/fgetallrestrolist/";

    public static String URL_GET_SEL_ORDERS = "http://drivers.lugmety.com/Apicalls/getorderbyrestrid";
    public static String URL_GET_SEL_F_ORDERS = "http://drivers.lugmety.com/Apicalls/fgetorderbyrestrid";

    public static String URL_F_ORDER_ASSIGN = "http://drivers.lugmety.com/Apicalls/freelanceaddorder";

    public static String URL_F_ORDER_UPDATE = "http://drivers.lugmety.com/Apicalls/freelanceupdateorder";

    public static String URL_PRICING_GEO = "http://drivers.lugmety.com/Apicalls/coordinates/";

    public static String URL_GET_POINTS = "http://drivers.lugmety.com/Apicalls/totalpoints";

    public static String URL_CHECK_F_ORDER = "http://drivers.lugmety.com/Apicalls/restaurantorder";

    public static String URL_GET_TRIP_HISTORY = "http://drivers.lugmety.com/Apicalls/orderswithstrend/";

    public static String URL_RESPOND_TO_ORDER = "http://drivers.lugmety.com/Apicalls/sponsoredupdateassignedorder";

    public static String URL_SYNC_DONE_ORDERS = "http://drivers.lugmety.com/Apicalls/sponsoredupdateorder";

    //--------------Lugemity Provided URL(s)----------------------------------------------

    //token-key phrase URL append
    public static String L_TOKEN_KEY = "token-key=32b11996bbdbd2dad406e851cb92f5db";
    public static String API_KEY = "32b11996bbdbd2dad406e851cb92f5db";

    //GET-webService-URL
    public static String LURL_GET_ALL_REST = "http://lugmety.com/dev/public/api/restaurants";
    public static String LURL_GET_REST_GEO = "http://lugmety.com/dev/public/api/restaurants/search-via-gps?radius=50&coordinates=";
    public static String LURL_GET_PENDING_ORDERS = "http://lugmety.com/dev/public/api/orders?token-key=32b11996bbdbd2dad406e851cb92f5db&order-status=pending&ResID=";
    public static String LURL_GET_ACCEPTED_ORDERS = "http://lugmety.com/dev/public/api/orders?token-key=32b11996bbdbd2dad406e851cb92f5db&order-status=accepted&ResID=";

    //POST-RESTful-webService-URL
    public static String LURL_ACCEPT_ORDER = "http://lugmety.com/dev/public/api/orders/assign/";
    public static String LURL_ORDER_EDIT = "http://lugmety.com/dev/public/api/orders/";
    public static String LURL_SET_COMPLETED = "http://lugmety.com/dev/public/api/orders/delivered?token-key=32b11996bbdbd2dad406e851cb92f5db";
}

