package com.yildiz.flatsearchapp;

import java.io.Serializable;

public class UserItem implements Serializable {
    String name;// changed from String nameSurname;
    String surname;
    String eMail;
    String phoneNr;
    String picture;
    int status;
    String department;
    String timeSpend;
    int distance;
    String userID;
    String isVerified;

    public UserItem(){

    }

    public UserItem(String userID, String name, String surname, String eMail, String phoneNr, String picture, int status, String department, String timeSpend, int distance, String isVerified) {
        this.name = name;
        this.surname = surname;
        this.eMail = eMail;
        this.phoneNr = phoneNr;
        this.picture = picture;
        this.status = status;
        this.department = department;
        this.timeSpend = timeSpend;
        this.distance = distance;
        this.userID = userID;
        this.isVerified = isVerified;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(String timeSpend) {
        this.timeSpend = timeSpend;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
