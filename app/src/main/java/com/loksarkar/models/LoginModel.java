package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginModel {

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


    public class FinalAry {
        @SerializedName("ReferralID")
        @Expose
        private String referralID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("MobileNo")
        @Expose
        private String mobileNo;
        @SerializedName("EmailAddress")
        @Expose
        private String emailAddress;

        public String getReferralID() {
            return referralID;
        }

        public void setReferralID(String referralID) {
            this.referralID = referralID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

    }


}
