package com.example.eco_app;

public class User {
    public String fullName, email;
    public User(){

    }

    public User(String fullName,String email){
        this.fullName = fullName;
        this.email =email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
