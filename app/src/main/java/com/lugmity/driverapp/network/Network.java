package com.lugmity.driverapp.network;

/**
 * Network-Constant: Class for holding HTTP Data and URL.
 */
public class Network {
    /**
     lugmety.vitessetec.com
     lugmety.drushtiindia.com
     */

    //--------------Drushti Provided URL(s)-----------------------------------------------

    public static String AUTH_LOGIN = "lugmetyKeys%20";
    public static String URL_LOGIN = "http://lugmety.vitessetec.com/Apicalls/userlogin/";

    public static String AUTH_REGISTER = "lugmetyKeys%221";
    public static String URL_REGISTER = "http://lugmety.vitessetec.com/Apicalls/addFDriver/";

    public static String AUTH_CHANGE_PASS = "lugmetyKeys%211";
    public static String URL_CHANGE_PASS = "http://lugmety.vitessetec.com/Apicalls/changepassword/";

    public static String URL_GET_IMG = "http://lugmety.vitessetec.com/Apicalls/freelanceprofile/";

    public static String URL_GET_ORDERS = "http://lugmety.vitessetec.com/Apicalls/getorderdetails/";

    public static String URL_ADD_ORDER = "http://lugmety.vitessetec.com/Apicalls/addorederdetails/";

    public static String URL_GET_ALL_REST = "http://lugmety.vitessetec.com/Apicalls/fgetallrestrolist/";

    public static String URL_GET_SEL_ORDERS = "http://lugmety.vitessetec.com/Apicalls/getorderbyrestrid";
    public static String URL_GET_SEL_F_ORDERS = "http://lugmety.vitessetec.com/Apicalls/fgetorderbyrestrid";

    public static String URL_F_ORDER_ASSIGN = "http://lugmety.vitessetec.com/Apicalls/freelanceaddorder";

    public static String URL_F_ORDER_UPDATE = "http://lugmety.vitessetec.com/Apicalls/freelanceupdateorder";

    public static String URL_PRICING_GEO = "http://lugmety.vitessetec.com/Apicalls/coordinates/";

    public static String URL_GET_POINTS = "http://lugmety.vitessetec.com/Apicalls/totalpoints";

    public static String URL_CHECK_F_ORDER = "http://lugmety.vitessetec.com/Apicalls/restaurantorder";

    public static String URL_GET_TRIP_HISTORY = "http://lugmety.vitessetec.com/Apicalls/orderswithstrend/";

    public static String URL_RESPOND_TO_ORDER = "http://lugmety.vitessetec.com/Apicalls/sponsoredupdateassignedorder";

    public static String URL_SYNC_DONE_ORDERS = "http://lugmety.vitessetec.com/Apicalls/sponsoredupdateorder";

    //--------------Lugemity Provided URL(s)----------------------------------------------

    //token-key phrase URL append
    public static String L_TOKEN_KEY = "token-key=32b11996bbdbd2dad406e851cb92f5db";
    public static String API_KEY = "32b11996bbdbd2dad406e851cb92f5db";

    //GET-webService-URL
    public static String LURL_GET_ALL_REST = "http://lugmety.go-demo.com/dev/public/api/restaurants";
    public static String LURL_GET_REST_GEO = "http://lugmety.go-demo.com/dev/public/api/restaurants/search-via-gps?radius=50&coordinates=";
    public static String LURL_GET_PENDING_ORDERS = "http://lugmety.go-demo.com/dev/public/api/orders?token-key=32b11996bbdbd2dad406e851cb92f5db&order-status=pending&ResID=";
    public static String LURL_GET_ACCEPTED_ORDERS = "http://lugmety.go-demo.com/dev/public/api/orders?token-key=32b11996bbdbd2dad406e851cb92f5db&order-status=accepted&ResID=";

    //POST-RESTful-webService-URL
    public static String LURL_ACCEPT_ORDER = "http://lugmety.go-demo.com/dev/public/api/orders/assign/";
    public static String LURL_ORDER_EDIT = "http://lugmety.go-demo.com/dev/public/api/orders/";
    public static String LURL_SET_COMPLETED = "http://lugmety.go-demo.com/dev/public/api/orders/delivered?token-key=32b11996bbdbd2dad406e851cb92f5db";
}

