package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationDataModel {

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

        @SerializedName("Date")
        @Expose
        private String date;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("FilePath")
        @Expose
        private String filePath;
        @SerializedName("FileType")
        @Expose
        private String fileType;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

    }
}
