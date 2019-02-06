package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HtmlModel {


    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("DataContent")
    @Expose
    private String dataContent;
    @SerializedName("ComplaintNo")
    @Expose
    private String complaintNo;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getComplaintNo() {
        return complaintNo;
    }

    public void setComplaintNo(String complaintNo) {
        this.complaintNo = complaintNo;
    }
}
