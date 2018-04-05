package com.curofy.anubhav.assignmentproject.models;

import java.io.Serializable;

/**
 * Created by anubhav on 5/4/18.
 */

public class SignInModel implements Serializable {
    //Initializing with default values available.
    private String country = "India";
    private String countryCode = "+91";
    private String contactNo;
    private String token;
    private String otp;

    public String getCountry() {
        return country;
    }

    public SignInModel setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public SignInModel setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getContactNo() {
        return contactNo;
    }

    public SignInModel setContactNo(String contactNo) {
        this.contactNo = contactNo;
        return this;
    }

    public String getToken() {
        return token;
    }

    public SignInModel setToken(String token) {
        this.token = token;
        return this;
    }

    public String getOtp() {
        return otp;
    }

    public SignInModel setOtp(String otp) {
        this.otp = otp;
        return this;
    }
}
