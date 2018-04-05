package com.curofy.anubhav.assignmentproject.views;

import com.curofy.anubhav.assignmentproject.models.CustomResponseModel;

/**
 * Created by anubhav on 5/4/18.
 */

public interface OtpView {
    boolean checkAllOtpFields();
    void fillOtpValueFromSms(int[] otp);
    void navigateToVerification();
    void toggleCallMeButtonToLetsGo();
    void toggleLetsGoButtonToCallMe();
    void toggleButtonToProgress();
    void toggleProgressToButton();
    void showErrorMessage(String str);
    void updateTokenModel(CustomResponseModel customResponseModel);

}
