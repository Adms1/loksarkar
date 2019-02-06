package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefferalUserListModel {


    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("FinalArray")
    @Expose
    private List<FinalArray> finalArray = null;

    @SerializedName("FinalAry")
    @Expose
    private List<FinalArray> finalArray1 = null;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
           this.success = success;
    }

    public List<FinalArray> getFinalArray() {
        return finalArray;
    }

    public void setFinalArray(List<FinalArray> finalArray) {
            this.finalArray = finalArray;
    }

    public List<FinalArray> getFinalArray1() {
        return finalArray1;
    }

    public void setFinalArray1(List<FinalArray> finalArray1) {
        this.finalArray1 = finalArray1;
    }


    public class FinalArray {

        @SerializedName("TotalInstallation")
        @Expose
        private int totalInstalltion;

        @SerializedName("RegisterDevice")
        @Expose
        private List<RegisterDevice> registerDevice = null;

        @SerializedName("RegisterComplaint")
        @Expose
        private List<RegisterComplaint> registerComplaint = null;

        public List<RegisterDevice> getRegisterDevice() {
            return registerDevice;
        }

        public void setRegisterDevice(List<RegisterDevice> registerDevice) {
            this.registerDevice = registerDevice;
        }

        public List<RegisterComplaint> getRegisterComplaint() {
            return registerComplaint;
        }

        public void setRegisterComplaint(List<RegisterComplaint> registerComplaint) {
            this.registerComplaint = registerComplaint;
        }

        public int getTotalInstalltion() {
            return totalInstalltion;
        }

        public void setTotalInstalltion(int totalInstalltion) {
            this.totalInstalltion = totalInstalltion;
        }

    }


    public class RegisterComplaint {

        @SerializedName("Rank")
        @Expose
        private Integer rank;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Code")
        @Expose
        private String code;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("Points")
        @Expose
        private Integer points;

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

    }


    public class RegisterDevice {

        @SerializedName("Rank")
        @Expose
        private Integer rank;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Code")
        @Expose
        private String code;
        @SerializedName("District")
        @Expose
        private String district;
        @SerializedName("Points")
        @Expose
        private Integer points;

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

    }
}
