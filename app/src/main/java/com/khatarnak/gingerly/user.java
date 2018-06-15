package com.khatarnak.gingerly;

public class user {
    private String email,status;

    public user(){

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {

        return email;
    }

    public String getStatus() {
        return status;
    }

    public user(String email, String status) {
        this.email = email;
        this.status = status;
    }
}
