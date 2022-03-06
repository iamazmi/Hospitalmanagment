package com.hospital.hospitalmanagment.model;

public class DoctorModel {
    private String DoctorName;
    private String Special;
    private String Profilepic;
    private String uid;

    public DoctorModel(String doctorName, String special, String profilepic, String uid) {
        DoctorName = doctorName;
        Special = special;
        Profilepic = profilepic;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public DoctorModel() {
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getSpecial() {
        return Special;
    }

    public void setSpecial(String special) {
        Special = special;
    }

    public String getProfilepic() {
        return Profilepic;
    }

    public void setProfilepic(String profilepic) {
        Profilepic = profilepic;
    }

    @Override
    public String toString() {
        return "DoctorModel{" +
                "DoctorName='" + DoctorName + '\'' +
                ", Special='" + Special + '\'' +
                ", Profilepic='" + Profilepic + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
