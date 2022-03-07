package com.hospital.hospitalmanagment.model;

public class Appointmentviewmodel {

    private String DateTimestamp;
    private String Patienuid;
    private String Status;
    private String timeSlot;

    private String PatitentName;
    private String phone;
    private String Profilepic;

    public Appointmentviewmodel(String dateTimestamp, String patienuid, String status, String timeSlot, String patitentName, String phone, String profilepic) {
        DateTimestamp = dateTimestamp;
        Patienuid = patienuid;
        Status = status;
        this.timeSlot = timeSlot;
        PatitentName = patitentName;
        this.phone = phone;
        Profilepic = profilepic;
    }

    public Appointmentviewmodel(String dateTimestamp, String status, String timeSlot) {
        DateTimestamp = dateTimestamp;
        Status = status;
        this.timeSlot = timeSlot;
    }



    @Override
    public String toString() {
        return "Appointmentviewmodel{" +
                "DateTimestamp='" + DateTimestamp + '\'' +
                ", Patienuid='" + Patienuid + '\'' +
                ", Status='" + Status + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", PatitentName='" + PatitentName + '\'' +
                ", phone='" + phone + '\'' +
                ", Profilepic='" + Profilepic + '\'' +
                '}';
    }


    public Appointmentviewmodel() {
    }


    public String getDateTimestamp() {
        return DateTimestamp;
    }

    public void setDateTimestamp(String dateTimestamp) {
        DateTimestamp = dateTimestamp;
    }

    public String getPatienuid() {
        return Patienuid;
    }

    public void setPatienuid(String patienuid) {
        Patienuid = patienuid;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getPatitentName() {
        return PatitentName;
    }

    public void setPatitentName(String patitentName) {
        PatitentName = patitentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilepic() {
        return Profilepic;
    }

    public void setProfilepic(String profilepic) {
        Profilepic = profilepic;
    }
}
