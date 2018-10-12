package com.loksarkar.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationTypeModel {
//=====Registration type =============
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Value")
    @Expose
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

 //========== District ============
   @SerializedName("DistrictName")
   @Expose
    private String districtName;
    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("WardStatus")
    @Expose
    private String WardStatus;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getWardStatus() {
        return WardStatus;
    }

    public void setWardStatus(String wardStatus) {
        WardStatus = wardStatus;
    }

    //====== Vidhan ======
  @SerializedName("AssemblyName")
  @Expose
  private String assemblyName;
    @SerializedName("Loksabha")
    @Expose
    private String loksabha;
    @SerializedName("AssemblyID")
    @Expose
    private String assemblyID;

    public String getAssemblyName() {
        return assemblyName;
    }

    public void setAssemblyName(String assemblyName) {
        this.assemblyName = assemblyName;
    }

    public String getLoksabha() {
        return loksabha;
    }

    public void setLoksabha(String loksabha) {
        this.loksabha = loksabha;
    }

    public String getAssemblyID() {
        return assemblyID;
    }

    public void setAssemblyID(String assemblyID) {
        this.assemblyID = assemblyID;
    }

  //=========== Taluka =========
  @SerializedName("TalukaName")
  @Expose
  private String talukaName;
    @SerializedName("TalukaID")
    @Expose
    private String talukaID;

    public String getTalukaName() {
        return talukaName;
    }

    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }

    public String getTalukaID() {
        return talukaID;
    }

    public void setTalukaID(String talukaID) {
        this.talukaID = talukaID;
    }


    //=========== City Ward =========
    @SerializedName("WardName")
    @Expose
    private String wardName;

    @SerializedName("WardID")
    @Expose
    private String wardID;

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    //=========== City Zone =========
    @SerializedName("ZoneName")
    @Expose
    private String zoneName;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }


    //=========== Dist Panchayat = =========
    @SerializedName("DistrictPanchayarID")
    @Expose
    private String DPId;

    @SerializedName("DistrictPanchayarName")
    @Expose
    private String DPName;

    public String getDPId() {
        return DPId;
    }

    public void setDPId(String DPId) {
        this.DPId = DPId;
    }

    public String getDPName() {
        return DPName;
    }

    public void setDPName(String DPName) {
        this.DPName = DPName;
    }



    //=========== Taluka Panchayat  =========
    @SerializedName("TalukaPanchayatID")
    @Expose
    private String TPId;

    @SerializedName("TalukaPanchayatName")
    @Expose
    private String TPName;

    public String getTPId() {
        return TPId;
    }

    public void setTPId(String DPId) {
        this.TPId = DPId;
    }

    public String getTPName() {
        return TPName;
    }

    public void setTPName(String DPName) {
        this.TPName = DPName;
    }








}
