package com.lugmity.driverapp.network;

/**
 * @class Response
 * @desc Class holding the API response statusCode and statusMessage.
 */
public class Response
{
    public static String S_200 = "Success";
    public static String S_203 = "Not Authenticated";
    public static String S_204 = "No Content";

    public static String S_400 = "Bad Request";
    public static String S_401 = "Unauthorized";
    public static String S_404 = "Not Found";

    public static String S_500 = "Internal Server Error";
    public static String S_504 = "Http Timeout";
    public static String S_507 = "Low Storage";
}
