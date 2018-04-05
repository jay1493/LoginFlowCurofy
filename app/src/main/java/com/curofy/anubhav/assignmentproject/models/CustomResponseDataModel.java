package com.curofy.anubhav.assignmentproject.models;

import java.io.Serializable;

/**
 * Created by anubhav on 5/4/18.
 */

public class CustomResponseDataModel implements Serializable {
    private int profile_exists;
    private String session_id;

    public int getProfile_exists() {
        return profile_exists;
    }

    public CustomResponseDataModel setProfile_exists(int profile_exists) {
        this.profile_exists = profile_exists;
        return this;
    }

    public String getSession_id() {
        return session_id;
    }

    public CustomResponseDataModel setSession_id(String session_id) {
        this.session_id = session_id;
        return this;
    }
}
