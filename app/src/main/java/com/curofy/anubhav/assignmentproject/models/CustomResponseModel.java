package com.curofy.anubhav.assignmentproject.models;

import java.io.Serializable;

/**
 * Created by anubhav on 5/4/18.
 */

public class CustomResponseModel implements Serializable {
    private int status;
    private CustomResponseDataModel data;

    public int getStatus() {
        return status;
    }

    public CustomResponseModel setStatus(int status) {
        this.status = status;
        return this;
    }

    public CustomResponseDataModel getData() {
        return data;
    }

    public CustomResponseModel setData(CustomResponseDataModel data) {
        this.data = data;
        return this;
    }
}
