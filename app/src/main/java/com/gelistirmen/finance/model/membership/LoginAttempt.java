package com.gelistirmen.finance.model.membership;

public class LoginAttempt {
    public String phoneNumber;
    public String password;
    public Device device;

    public LoginAttempt(String phoneNumber, String password, Device device) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.device = device;
    }
}
