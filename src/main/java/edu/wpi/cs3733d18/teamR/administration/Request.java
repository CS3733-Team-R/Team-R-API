package edu.wpi.cs3733d18.teamR.administration;

public class Request {
    String name;
    String DOB;
    String medication;
    String address;
    String location;
    String allergies;
    String conditions;
    String date;
    String time;
    String phone;
    String signature;
    String reqID;
    int SSN;

    public Request(String name, String DOB, String medication, String address, String location, String allergies, String conditions, String date, String time, String phone, String signature, String reqID, int SSN) {
        this.name = name;
        this.DOB = DOB;
        this.medication = medication;
        this.address = address;
        this.location = location;
        this.allergies = allergies;
        this.conditions = conditions;
        this.date = date;
        this.time = time;
        this.phone = phone;
        this.signature = signature;
        this.reqID = reqID;
        this.SSN = SSN;
    }

    public String getName() {
        return name;
    }

    public String getDOB() {
        return DOB;
    }

    public String getMedication() {
        return medication;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getConditions() {
        return conditions;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPhone() {
        return phone;
    }

    public String getSignature() {
        return signature;
    }

    public String getReqID() {
        return reqID;
    }

    public int getSSN() {
        return SSN;
    }
}




