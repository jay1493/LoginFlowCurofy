package com.curofy.anubhav.assignmentproject.presenters;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.curofy.anubhav.assignmentproject.R;
import com.curofy.anubhav.assignmentproject.models.CustomResponseModel;
import com.curofy.anubhav.assignmentproject.models.SignInModel;
import com.curofy.anubhav.assignmentproject.network.RetrofitDao;
import com.curofy.anubhav.assignmentproject.network.RetrofitInstance;
import com.curofy.anubhav.assignmentproject.views.OtpView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anubhav on 5/4/18.
 */

public class OtpPresenter {

    private RetrofitDao retrofitDao;
    private Context context;
    private OtpView otpView;
    private CustomResponseModel dummyResponseModel;
    public OtpPresenter(Context context, OtpView otpView) {
        this.context = context;
        this.otpView = otpView;
        retrofitDao = RetrofitInstance.getRetrofitInstance().create(RetrofitDao.class);
    }

    public void processSms(String message) {
       if(!TextUtils.isEmpty(message)){
           int otp = Integer.parseInt(message.replaceAll("[^0-9]",""));
           List<Integer> digits = new ArrayList<Integer>();
           while (otp > 0) {
               digits.add(otp%10);
               otp/=10;
           }
           //Digits are reversed
           int[] otpArr = new int[digits.size()];
           for(int i =0;i<digits.size();i++){
               otpArr[i] = digits.get((digits.size()-1)-i);
           }
           otpView.fillOtpValueFromSms(otpArr);

       }
    }

    public void processVerification(SignInModel userModel,CustomResponseModel customResponseModel) {
        otpView.toggleButtonToProgress();

        Call<CustomResponseModel> customResponseModelCall = retrofitDao.verifyLogin(userModel.getContactNo(),userModel.getCountryCode(),userModel.getOtp(),customResponseModel.getData().getSession_id());
        customResponseModelCall.enqueue(new Callback<CustomResponseModel>() {
            @Override
            public void onResponse(Call<CustomResponseModel> call, Response<CustomResponseModel> response) {
                dummyResponseModel = response.body();
                if(dummyResponseModel!=null && dummyResponseModel.getStatus() == 1){
                    //Success
                    otpView.navigateToVerification();

                }else{
                    otpView.toggleProgressToButton();
                    otpView.showErrorMessage(context.getResources().getString(R.string.invalid_request_try_again));
                }
            }

            @Override
            public void onFailure(Call<CustomResponseModel> call, Throwable t) {
                otpView.toggleProgressToButton();
                otpView.showErrorMessage(context.getResources().getString(R.string.network_failure_try_again));
            }
        });

    }

    public void resendOtp(SignInModel userModel) {
        Call<CustomResponseModel> customResponseModelCall = retrofitDao.generateOtp(userModel.getContactNo(),userModel.getCountryCode());
        customResponseModelCall.enqueue(new Callback<CustomResponseModel>() {
            @Override
            public void onResponse(Call<CustomResponseModel> call, Response<CustomResponseModel> response) {
                CustomResponseModel customResponseModel = response.body();
                if(customResponseModel!=null && customResponseModel.getStatus() == 1){
                    //Success
                    otpView.updateTokenModel(customResponseModel);

                }else{
                    otpView.showErrorMessage(context.getResources().getString(R.string.invalid_request_try_again));
                }
            }

            @Override
            public void onFailure(Call<CustomResponseModel> call, Throwable t) {
                otpView.showErrorMessage(context.getResources().getString(R.string.network_failure_try_again));
            }
        });
    }
}
