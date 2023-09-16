package com.mypackage.bookloop;

public class BLUserAccount {
    private String userName, userEmail, userPhone;

    public BLUserAccount(String userName, String userEmail, String userPhone) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        //this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getuserPhone() {
        return userPhone;
    }

}
