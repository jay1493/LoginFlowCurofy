package com.curofy.anubhav.assignmentproject.views;

import com.curofy.anubhav.assignmentproject.models.CustomResponseModel;

/**
 * Created by anubhav on 5/4/18.
 */

public interface MainSignInView {
    void navigateToOtp(CustomResponseModel customResponseModel);
    void showErrorMessage(String errStr);
    void resetContactField();
    void countrySelected(int pos,String name);
    void toggleSignInBtnToProgress();
    void toggleSignInBtnFromProgress();
    void prefillContactNoIfAvail(String contact);
}
