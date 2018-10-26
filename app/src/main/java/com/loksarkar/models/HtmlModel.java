package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HtmlModel {

    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("FinalArray")
    @Expose
    private String finalArray;

    public String getSuccess() {
            return success;
        }
        public void setSuccess(String success) {
            this.success = success;
        }

        public String getFinalArray() {
            return finalArray;
        }

        public void setFinalArray(String finalArray) {
            this.finalArray = finalArray;
        }


}
