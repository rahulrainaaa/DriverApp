package com.lugmity.driverapp.responseparser;

import com.lugmity.driverapp.constants.Constants;
import com.lugmity.driverapp.model.FreelanceUserData;
import com.lugmity.driverapp.model.SponsoredUserData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @class LoginResponseParser
 * @desc Response Parser Class for parsing the response data from http web API.
 */
public class LoginResponseParser {

    /**
     * @param json
     * @return 0=success, 1=JSONException, 2=GenericException
     * @method parserFreelancerResponse
     * @desc Method to parse Freelancer login API response.
     */
    public int parserFreelancerResponse(JSONObject json) {
        int status = 0;
        try {
            Constants.freelanceUserData = new FreelanceUserData();
            Constants.sponsoredUserData = null;

            Constants.freelanceUserData.respLoginId = json.get("loginid").toString();
            Constants.freelanceUserData.respUserId = json.get("user_id").toString();
            Constants.freelanceUserData.respUserName = json.get("username").toString();
            Constants.freelanceUserData.respPassword = json.get("password").toString();
            Constants.freelanceUserData.respLastLogin = json.get("lastlogin").toString();
            Constants.freelanceUserData.respIsActive = json.get("is_active").toString();
            Constants.freelanceUserData.respUserType = json.get("usertype").toString();
            Constants.freelanceUserData.respAuthentication = json.get("authentication").toString();
            Constants.freelanceUserData.respCountryCode = json.get("country_code").toString();

            Constants.freelanceUserData.respFId = json.get("fid").toString();
            Constants.freelanceUserData.respFName = json.get("fname").toString();
            Constants.freelanceUserData.respFGender = json.get("fgender").toString();
            Constants.freelanceUserData.respFMobile = json.get("fmobile").toString();
            Constants.freelanceUserData.respFEmail = json.get("femail").toString();
            Constants.freelanceUserData.respFAddress = json.get("faddress").toString();
            Constants.freelanceUserData.respFIdNo = json.get("fidno").toString();
            Constants.freelanceUserData.respFDob = json.get("fdob").toString();
            Constants.freelanceUserData.respFPhoto = json.get("fphoto").toString();
            Constants.freelanceUserData.respFRegistrationDate = json.get("fregistration_date").toString();
            Constants.freelanceUserData.respFApproval = json.get("fapproval").toString();
            Constants.freelanceUserData.respModified = json.get("modified").toString();

            Constants.freelanceUserData.respEarnedMoney = json.get("earned_money").toString();
            Constants.freelanceUserData.respEarnedPoints = json.get("points").toString();
            Constants.freelanceUserData.respTotalOrders = json.get("total_orders").toString();
        } catch (JSONException jsonE) {
            status = 1;
        } catch (Exception e) {
            status = 2;
        } finally {
            json = null;
        }
        return status;

    }

    /**
     * @param json
     * @return 0=success, 1=JSONException, 2=GenericException
     * @method parserSponsoredResponse
     * @desc Method to parse Sponsored login API response.
     */
    public int parserSponsoredResponse(JSONObject json) {
        int status = 0;
        try {
            Constants.freelanceUserData = null;
            Constants.sponsoredUserData = new SponsoredUserData();

            Constants.sponsoredUserData.respLoginId = json.get("loginid").toString();
            Constants.sponsoredUserData.respUserId = json.get("user_id").toString();
            Constants.sponsoredUserData.respUserName = json.get("username").toString();
            Constants.sponsoredUserData.respPassword = json.get("password").toString();
            Constants.sponsoredUserData.respLastLogin = json.get("lastlogin").toString();
            Constants.sponsoredUserData.respIsActive = json.get("is_active").toString();
            Constants.sponsoredUserData.respUserType = json.get("usertype").toString();
            Constants.sponsoredUserData.respAuthentication = json.get("authentication").toString();

            Constants.sponsoredUserData.respSID = json.get("sid").toString();
            Constants.sponsoredUserData.respSName = json.get("sname").toString();
            Constants.sponsoredUserData.respSAddress = json.get("saddress").toString();
            Constants.sponsoredUserData.respSCity = json.get("scity").toString();
            Constants.sponsoredUserData.respSMobile = json.get("smobile").toString();
            Constants.sponsoredUserData.respSRegistrationDate = json.getString("sregistration_date").trim();
            Constants.sponsoredUserData.respSApproval = json.getString("sapproval").trim();

            Constants.sponsoredUserData.respRestoId = json.getString("resto_id").trim();
            Constants.sponsoredUserData.respRestroName = json.getString("restro_name").trim();
            Constants.sponsoredUserData.respRestroUID = json.getString("restro_uniqueId").trim();

        } catch (JSONException jsonE) {
            status = 1;
        } catch (Exception e) {
            status = 2;
        } finally {
            json = null;
        }
        return status;

    }
}
