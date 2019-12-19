package com.sb.ubergodriver.viewModel.login;

import com.google.gson.JsonElement;

import com.sb.ubergodriver.apiConstants.ApiCallInterface;
import com.sb.ubergodriver.model.alert.AlertRequest;
import com.sb.ubergodriver.model.chat.ChatHistoryRequest;
import com.sb.ubergodriver.model.forgot.EmailOtpRequest;
import com.sb.ubergodriver.model.forgot.ForgotPasswordRequest;
import com.sb.ubergodriver.model.requests.*;
import com.sb.ubergodriver.model.tripHistory.TriphistoryRequest;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

public class Repository {

    public ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    /*
     * method to call login api
     * */
    public Observable<JsonElement> executeLogin(LoginRequest loginRequest) {
        return apiCallInterface.login(loginRequest);
    }

    /*
     * method to call login api
     * */
    public Observable<JsonElement> executeMobileLogin(LoginWithMobileRequest loginRequest) {
        return apiCallInterface.loginwithMobile(loginRequest);
    }

    /*
     * method to call edit profile api
     * */
    public Observable<JsonElement> executeEditProfile(EditProfilerequest loginRequest) {
        return apiCallInterface.edirProfile(loginRequest);
    }


    /*
     * method to call edit Change Email api
     * */
    public Observable<JsonElement> executeUpdateEmail(UpdateEmailRequest loginRequest) {
        return apiCallInterface.changeEmail(loginRequest);
    }

    /*
     * method to call edit Check phone status api
     * */
    public Observable<JsonElement> executeCheckPhoneStatus(PhoneStatusRequest loginRequest) {
        return apiCallInterface.checkPhoneSatus(loginRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeChangePasswoed(ChangePasswordRequest loginRequest) {
        return apiCallInterface.changePassword(loginRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeForgotChangePasswoed(ForgotPasswordRequest loginRequest) {
        return apiCallInterface.forgotPassword(loginRequest);
    }

    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeEmailOtp(EmailOtpRequest loginRequest) {
        return apiCallInterface.emailOtp(loginRequest);
    }

    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeVerifyOtp(EmailOtpRequest loginRequest) {
        return apiCallInterface.verifyOtp(loginRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeChangeDriverStatus(ChangeDriverStatusRequest loginRequest) {
        return apiCallInterface.changeDriverStatus(loginRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeUploadImage(MultipartBody.Part loginRequest) {
        return apiCallInterface.uploadImage(loginRequest);
    }



    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeGetDocument(String loginRequest) {
        return apiCallInterface.getDocument(loginRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeGetVehicle(String loginRequest) {
        return apiCallInterface.getVehicle(loginRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeUserRating(String loginRequest) {
        return apiCallInterface.getUserRating(loginRequest);
    }

    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executesetRatingToUser(UserRatingRequest loginRequest) {
        return apiCallInterface.getUserRating(loginRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeTripHistory(TriphistoryRequest loginRequest) {
        return apiCallInterface.gettripHistoryrequest(loginRequest);
    }

    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeRegister(RequestBody dataBody,MultipartBody.Part driverLicence, MultipartBody.Part Tzini,MultipartBody.Part numberPlate,MultipartBody.Part rc) {
        return apiCallInterface.onProfilePicUpload(dataBody,driverLicence,Tzini,numberPlate,rc);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> checkRegisterNumber(RegistrerCheckStatusRequest registerRequest) {
        return apiCallInterface.checkRegisterNumber(registerRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeChatHistory(ChatHistoryRequest registerRequest) {
        return apiCallInterface.executeChatHistory(registerRequest);
    }

    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeAlert(AlertRequest registerRequest) {
        return apiCallInterface.executeAlert(registerRequest);
    }


    /*
     * method to call edit Change password api
     * */
    public Observable<JsonElement> executeSupport(SupportRequest registerRequest) {
        return apiCallInterface.executeSuppoert(registerRequest);
    }

}
