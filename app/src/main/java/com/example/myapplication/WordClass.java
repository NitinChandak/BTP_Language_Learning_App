package com.example.myapplication;

public class WordClass {
    public  WordClass() {}
    String First_name, LastName, Email, Avatar;

    /*public WordClass(String firstName, String lastName, String email, String avatar) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.Avatar = avatar;
    }*/

    public String getFirst_name() {
        return First_name;
    }

    public void setFirst_name(String first_name) {
        this.First_name = first_name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getEmail() { return Email;}

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        this.Avatar = avatar;
    }

}
