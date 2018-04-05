package com.curofy.anubhav.assignmentproject.network;

import com.curofy.anubhav.assignmentproject.models.CustomResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by anubhav on 5/4/18.
 */

public interface RetrofitDao {

    @FormUrlEncoded
    @POST("generate_otp.json")
    Call<CustomResponseModel> generateOtp(@Field("mobile_no") String mobile_no, @Field("country_code") String country_code);

    @FormUrlEncoded
    @POST("login_app.json")
    Call<CustomResponseModel> verifyLogin(@Field("mobile_no") String mobile_no,@Field("country_code") String country_code,@Field("otp") String otp,@Field("session_id") String session_id);
}
