package com.lugmity.driverapp.network;

/**
 * Network-Constant: Class for holding HTTP Data and URL.
 */
public class Network {
    /**
     drivers.lugmety.com
     www.lugmety.com
     */

    //--------------Drushti Provided URL(s)-----------------------------------------------

    public static String AUTH_LOGIN = "lugmetyKeys%20";
    public static String URL_LOGIN = "https://drivers.lugmety.com/Apicalls/userlogin/";

    public static String AUTH_REGISTER = "lugmetyKeys%221";
    public static String URL_REGISTER = "https://drivers.lugmety.com/Apicalls/addFDriver/";

    public static String AUTH_CHANGE_PASS = "lugmetyKeys%211";
    public static String URL_CHANGE_PASS = "https://drivers.lugmety.com/Apicalls/changepassword/";

    public static String URL_GET_IMG = "https://drivers.lugmety.com/Apicalls/freelanceprofile/";

    public static String URL_GET_ORDERS = "https://drivers.lugmety.com/Apicalls/getorderdetails/";

    public static String URL_ADD_ORDER = "https://drivers.lugmety.com/Apicalls/addorederdetails/";

    public static String URL_GET_ALL_REST = "https://drivers.lugmety.com/Apicalls/fgetallrestrolist/";

    public static String URL_GET_SEL_ORDERS = "https://drivers.lugmety.com/Apicalls/getorderbyrestrid";
    public static String URL_GET_SEL_F_ORDERS = "https://drivers.lugmety.com/Apicalls/fgetorderbyrestrid";

    public static String URL_F_ORDER_ASSIGN = "https://drivers.lugmety.com/Apicalls/freelanceaddorder";

    public static String URL_F_ORDER_UPDATE = "https://drivers.lugmety.com/Apicalls/freelanceupdateorder";

    public static String URL_PRICING_GEO = "https://drivers.lugmety.com/Apicalls/coordinates/";

    public static String URL_GET_POINTS = "https://drivers.lugmety.com/Apicalls/totalpoints";

    public static String URL_CHECK_F_ORDER = "https://drivers.lugmety.com/Apicalls/restaurantorder";

    public static String URL_GET_TRIP_HISTORY = "https://drivers.lugmety.com/Apicalls/orderswithstrend/";

    public static String URL_RESPOND_TO_ORDER = "https://drivers.lugmety.com/Apicalls/sponsoredupdateassignedorder";

    public static String URL_SYNC_DONE_ORDERS = "https://drivers.lugmety.com/Apicalls/sponsoredupdateorder";

    //--------------Lugemity Provided URL(s)----------------------------------------------

    //token-key phrase URL append
    public static String L_TOKEN_KEY = "token-key=32b11996bbdbd2dad406e851cb92f5db";
    public static String API_KEY = "32b11996bbdbd2dad406e851cb92f5db";

    //GET-webService-URL
    public static String LURL_GET_ALL_REST = "https://www.lugmety.com/api/restaurants";
    public static String LURL_GET_REST_GEO = "https://www.lugmety.com/api/restaurants/search-via-gps?radius=50&coordinates=";
    public static String LURL_GET_PENDING_ORDERS = "https://www.lugmety.com/orders?token-key=32b11996bbdbd2dad406e851cb92f5db&order-status=pending&ResID=";
    public static String LURL_GET_ACCEPTED_ORDERS = "https://www.lugmety.com/api/orders?token-key=32b11996bbdbd2dad406e851cb92f5db&order-status=accepted&ResID=";

    //POST-RESTful-webService-URL
    public static String LURL_ACCEPT_ORDER = "https://www.lugmety.com/api/orders/assign/";
    public static String LURL_ORDER_EDIT = "https://www.lugmety.com/api/orders/";
    public static String LURL_SET_COMPLETED = "https://www.lugmety.com/api/orders/delivered?token-key=32b11996bbdbd2dad406e851cb92f5db";
}

