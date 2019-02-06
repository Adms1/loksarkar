package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplaintsModel {

    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("FinalAry")
    @Expose
    private List<FinalAry> finalAry = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<FinalAry> getFinalAry() {
        return finalAry;
    }

    public void setFinalAry(List<FinalAry> finalAry) {
        this.finalAry = finalAry;
    }

    public static class FinalAry {

        @SerializedName("Number")
        @Expose
        private String number;
        @SerializedName("Date")
        @Expose
        private String date;
        @SerializedName("ComplaintStatus")
        @Expose
        private String complaintStatus;
        @SerializedName("URL")
        @Expose
        private String uRL;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getComplaintStatus() {
            return complaintStatus;
        }

        public void setComplaintStatus(String complaintStatus) {
            this.complaintStatus = complaintStatus;
        }

        public String getURL() {
            return uRL;
        }

        public void setURL(String uRL) {
            this.uRL = uRL;
        }

    }
}
