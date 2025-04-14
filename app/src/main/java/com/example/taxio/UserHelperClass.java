package com.example.taxio;

public class UserHelperClass {
    String username,email,phoneno,password;

    public  UserHelperClass(){

    }

    public UserHelperClass(String username, String email, String phoneno, String password) {
        this.username = username;
        this.email = email;
        this.phoneno = phoneno;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
