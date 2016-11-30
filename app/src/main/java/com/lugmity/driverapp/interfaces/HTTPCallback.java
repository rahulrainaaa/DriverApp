package com.lugmity.driverapp.interfaces;


/**
 * @interface HTTPCallback
 * @desc Interface for implementing HTTPTask Server Response Callbacks in Activity.
 */
public interface HTTPCallback
{

    /**
     * @desc Callback method for success HTTPTask from Server.
     * @param statusCode Success Response Code from Server .
     * @param statusMessage Success Message from Server.
     * @param data JSON String data from Server.
     * @param code Request code Tag for tracking.
     */
    public void onSuccess(int statusCode, String statusMessage, String data, int code);

    /**
     * @method Callback method for Failure from Server.
     * @param statusCode Failure Response Code from Server.
     * @param statusMessage Failure Response Message from Server.
     * @param code Request code Tag for tracking.
     */
    public void onFailure(int statusCode, String statusMessage, int code);

    /**
     * @method Callback method for HTTPTask Exception handling.
     * @param statusCode -1
     * @param statusMessage EXCEPTION.
     * @param code Request code Tag for tracking.
     */
    public void onError(int statusCode, String statusMessage, int code);

}
