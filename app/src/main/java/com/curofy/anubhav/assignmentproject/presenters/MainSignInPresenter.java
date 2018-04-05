package com.curofy.anubhav.assignmentproject.presenters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.curofy.anubhav.assignmentproject.R;
import com.curofy.anubhav.assignmentproject.models.CustomResponseModel;
import com.curofy.anubhav.assignmentproject.models.SignInModel;
import com.curofy.anubhav.assignmentproject.network.RetrofitDao;
import com.curofy.anubhav.assignmentproject.network.RetrofitInstance;
import com.curofy.anubhav.assignmentproject.views.MainSignInView;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anubhav on 5/4/18.
 */

public class MainSignInPresenter {

    private RetrofitDao retrofitDao;
    private Context context;
    private MainSignInView mainSignInView;
    private String[] countries;
    private String[] countryCodes;
    private SignInModel signInModel;
    private CustomResponseModel customResponseModel;

    public MainSignInPresenter(Context context, MainSignInView mainSignInView) {
        this.context = context;
        this.mainSignInView = mainSignInView;
        this.countries = context.getResources().getStringArray(R.array.countries);
        this.countryCodes = context.getResources().getStringArray(R.array.countryCodes);
        signInModel = new SignInModel();
        retrofitDao = RetrofitInstance.getRetrofitInstance().create(RetrofitDao.class);
    }

    public void processCountrySpinner() {
        new LovelyChoiceDialog(context)
                .setTopColorRes(R.color.darkGreen)
                .setTitle(R.string.select_country)
                .setMessage(R.string.countries)
                .setItems(getCountries(), new LovelyChoiceDialog.OnItemSelectedListener<String>() {

                    @Override
                    public void onItemSelected(int position, String item) {
                        mainSignInView.countrySelected(position, item);

                    }
                })
                .show();
    }


    public void processSignInRequest(String contactNo) {
        if (!TextUtils.isEmpty(contactNo)) {
            if (isContactValid(contactNo)) {
                getSignInModel().setContactNo(contactNo);
                mainSignInView.toggleSignInBtnToProgress();
                //Hit Service, show progress bar...
                Call<CustomResponseModel> customResponseModelCall = retrofitDao.generateOtp(getSignInModel().getContactNo(),getSignInModel().getCountryCode());
                customResponseModelCall.enqueue(new Callback<CustomResponseModel>() {
                    @Override
                    public void onResponse(Call<CustomResponseModel> call, Response<CustomResponseModel> response) {
                        customResponseModel = response.body();
                        if(customResponseModel!=null && customResponseModel.getStatus() == 1){
                            //Success
                            mainSignInView.navigateToOtp(customResponseModel);

                        }else{
                            mainSignInView.toggleSignInBtnFromProgress();
                            mainSignInView.showErrorMessage(context.getResources().getString(R.string.invalid_request_try_again));
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomResponseModel> call, Throwable t) {
                        mainSignInView.toggleSignInBtnFromProgress();
                        mainSignInView.showErrorMessage(context.getResources().getString(R.string.network_failure_try_again));
                    }
                });
            }
        } else {
            mainSignInView.showErrorMessage(context.getResources().getString(R.string.contact_no_cannot_be_left_empty));
        }


    }

    private boolean isContactValid(String contactNo) {
        switch (getSignInModel().getCountryCode()) {
            case "+91":
                if (contactNo.length() < 10) {
                    mainSignInView.showErrorMessage(context.getResources().getString(R.string.invalid_contact_no));
                    return false;
                }
                break;
        }
        return true;
    }

    public void processDeviceContactNo() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if ( ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            }
            String contact = telephonyManager.getLine1Number();
            if(!TextUtils.isEmpty(contact.trim())){
                mainSignInView.prefillContactNoIfAvail(contact);
            }else{
                return;
            }
        }
    }

    public String[] getCountries() {
        return countries;
    }

    public String[] getCountryCodes() {
        return countryCodes;
    }

    public SignInModel getSignInModel() {
        return signInModel;
    }

    public CustomResponseModel getCustomResponseModel() {
        return customResponseModel;
    }
}
