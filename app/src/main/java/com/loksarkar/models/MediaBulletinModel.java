package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MediaBulletinModel {


    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("TotalPage")
    @Expose
    private int totalcont;
    @SerializedName("TotalRow")
    @Expose
    private int totalrow;
    @SerializedName("FinalArray")
    @Expose
    private ArrayList<FinalArray> finalArray = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<FinalArray> getFinalArray() {
        return finalArray;
    }

    public void setFinalArray(ArrayList<FinalArray> finalArray) {
        this.finalArray = finalArray;
    }

    public int getTotalcont() {
        return totalcont;
    }

    public void setTotalcont(int totalcont) {
        this.totalcont = totalcont;
    }

    public int getTotalrow() {
        return totalrow;
    }

    public void setTotalrow(int totalrow) {
        this.totalrow = totalrow;
    }

    public static class FinalArray {

        @SerializedName("PK_BulletinID")
        @Expose
        private String pKBulletinID;
        @SerializedName("Bulletin_CDate")
        @Expose
        private String bulletinCDate;
        @SerializedName("Bulletin_Title")
        @Expose
        private String bulletinTitle;
        @SerializedName("Bulletin_Description")
        @Expose
        private String bulletinDescription;
        @SerializedName("Bulletin_FileName")
        @Expose
        private String bulletinFileName;
        @SerializedName("FileType")
        @Expose
        private String fileType;

        public String getPKBulletinID() {
            return pKBulletinID;
        }

        public void setPKBulletinID(String pKBulletinID) {
            this.pKBulletinID = pKBulletinID;
        }

        public String getBulletinCDate() {
            return bulletinCDate;
        }

        public void setBulletinCDate(String bulletinCDate) {
            this.bulletinCDate = bulletinCDate;
        }

        public String getBulletinTitle() {
            return bulletinTitle;
        }

        public void setBulletinTitle(String bulletinTitle) {
            this.bulletinTitle = bulletinTitle;
        }

        public String getBulletinDescription() {
            return bulletinDescription;
        }

        public void setBulletinDescription(String bulletinDescription) {
            this.bulletinDescription = bulletinDescription;
        }

        public String getBulletinFileName() {
            return bulletinFileName;
        }

        public void setBulletinFileName(String bulletinFileName) {
            this.bulletinFileName = bulletinFileName;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

    }

}



