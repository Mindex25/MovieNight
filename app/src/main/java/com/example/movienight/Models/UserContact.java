package com.example.movienight.Models;

import java.io.Serializable;
import java.util.List;

public class UserContact implements Serializable {

    private boolean status;
    private String fullname;
    private String email;
    private String id;

    public UserContact() {}

    public UserContact(String fullname, String email, boolean status, String id) {
        this.fullname = fullname;
        this.email = email;
        this.status = status;
        this.id = id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static UserContact getUserContactbyEmail(List<UserContact> contactList, String email) {
        for (UserContact userContact : contactList) {
            if (userContact.email.equals(email)) {
                return userContact;
            }
        }
        return null;
    }

    public  static boolean contactExists(List<UserContact> contactList, String contactEmail){
        for (UserContact userContact : contactList) {
            if (userContact.email.equals(contactEmail)) {
                return true;
            }
        }
        return false;
    }
}