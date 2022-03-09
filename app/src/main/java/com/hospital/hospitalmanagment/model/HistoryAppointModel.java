package com.hospital.hospitalmanagment.model;

public class HistoryAppointModel {

    private String docName;
    private String patName;
    private String appointDateTime;
    private String appointStatus;
    private String docProfile;
    private String patProfile;
    private String docSpec;
    private String docIUid;
    private int lvl;

    public int getLvl() {
        return lvl;
    }

    public HistoryAppointModel(String docIUid, int lvl) {
        this.docIUid = docIUid;
        this.lvl = lvl;
    }
    public HistoryAppointModel() {
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    @Override
    public String toString() {
        return "HistoryAppointModel{" +
                "docName='" + docName + '\'' +
                ", patName='" + patName + '\'' +
                ", appointDateTime='" + appointDateTime + '\'' +
                ", appointStatus='" + appointStatus + '\'' +
                ", docProfile='" + docProfile + '\'' +
                ", patProfile='" + patProfile + '\'' +
                ", docSpec='" + docSpec + '\'' +
                ", docIUid='" + docIUid + '\'' +
                '}';
    }

    public String getDocIUid() {
        return docIUid;
    }

    public void setDocIUid(String docIUid) {
        this.docIUid = docIUid;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getAppointDateTime() {
        return appointDateTime;
    }

    public void setAppointDateTime(String appointDateTime) {
        this.appointDateTime = appointDateTime;
    }

    public String getAppointStatus() {
        return appointStatus;
    }

    public void setAppointStatus(String appointStatus) {
        this.appointStatus = appointStatus;
    }

    public String getDocProfile() {
        return docProfile;
    }

    public void setDocProfile(String docProfile) {
        this.docProfile = docProfile;
    }

    public String getPatProfile() {
        return patProfile;
    }

    public void setPatProfile(String patProfile) {
        this.patProfile = patProfile;
    }

    public String getDocSpec() {
        return docSpec;
    }

    public void setDocSpec(String docSpec) {
        this.docSpec = docSpec;
    }


}
