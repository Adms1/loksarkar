package com.loksarkar.api;

import com.google.gson.JsonObject;
import com.loksarkar.models.ComplaintsModel;
import com.loksarkar.models.HtmlModel;
import com.loksarkar.models.LoginModel;
import com.loksarkar.models.MediaBulletinModel;
import com.loksarkar.models.NotificationDataModel;
import com.loksarkar.models.OTPModel;
import com.loksarkar.models.RefferalUserListModel;
import com.loksarkar.models.RegistrationModel;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.Part;


public interface WebServices {

    @FormUrlEncoded
    @POST("/GetRegistrationType")
    public void getRegistrationType(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetDistrictList")
    public void getDistrictList(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetAssemblyLoksabhaByDistrict")
    public void getAssemblyLoksabhaByDistrict(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetTaluka")
    public void getTaluka(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetCityWard")
    public void getCityWard(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetCityZoneByCityWard")
    public void getCityZoneByCityWard(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetDistrictPanchayat")
    public void getDistrictPanchayatORWardd(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetTalukaPanchayat")
    public void getTalukaPanchayat(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/GetDistrictWard")
    public void getDistrictWard(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/SaveRegistrationForm")
    public void saveRegistrationForm(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/SendOTP")
    public void sendOTP(@FieldMap Map<String, String> map, Callback<OTPModel> callback);

    @FormUrlEncoded
    @POST("/SignUpUser")
    public void signUpUser(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/LoginUser")
    public void signInUser(@FieldMap Map<String, String> map, Callback<LoginModel> callback);

    @FormUrlEncoded
    @POST("/NotificationDetail")
    public void notificationDetail(@FieldMap Map<String, String> map, Callback<NotificationDataModel> callback);

    @FormUrlEncoded
    @POST("/RegisterDevice")
    public void registerDevice(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);


    @FormUrlEncoded
    @POST("/GetMediaBulletin")
    public void getMediaBulletin(@FieldMap Map<String, String> map, Callback<MediaBulletinModel> callback);

    @FormUrlEncoded
    @POST("/Media_SendOTP")
    public void sendMediaOTP(@FieldMap Map<String, String> map, Callback<OTPModel> callback);

    @FormUrlEncoded
    @POST("/Media_SignUpUser")
    public void mediaSignUp(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/Media_SignUpUserNew")
    public void mediaSignUpNew(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @Multipart
    @POST(AppConfiguration.IMAGE_UPLOAD_URL)
    Call<JsonObject> uploadImage(@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("/GetLatestVersion")
    public void getLatestVersion(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/ReferralDetail")
    public void referralDetail(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/RegisterDeviceNew")
    public void registerDeviceNew(@FieldMap Map<String, String> map, Callback<RegistrationModel> callback);

    @FormUrlEncoded
    @POST("/LoksevakRequest")
    public void getComplaintsList(@FieldMap Map<String, String> map, Callback<ComplaintsModel> callback);


    @FormUrlEncoded
    @POST("/TopRefferalUser")
    public void topRefferalUser(@FieldMap Map<String, String> map, Callback<RefferalUserListModel> callback);

//    @GET("/NewService")
//    public void getHtmlContent(Callback<HtmlModel> callback);

    //@FormUrlEncoded
//    @GET("/GetComplaintData")
//    public void getHtmlContent(@Query("Req") String address, Callback<HtmlModel> callback);

    @GET("/GetComplaintDataNew")
    public void getHtmlContent(@Query("Req") String address, Callback<HtmlModel> callback);


    @FormUrlEncoded
    @POST("/GetUserPoint")
    public void getUserPoint(@FieldMap Map<String, String> map, Callback<RefferalUserListModel> callback);

}
