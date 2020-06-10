package com.gelistirmen.finance.model.membership;

public class RegisterAttempt {
    public String name;
    public String phoneNumber;
    public String companyName;
    public String password;
    public String passwordRepeat;
    public boolean userAgreementAccepted;
    public boolean dataProtectionPermisionAccepted;
    public Device device;

    public RegisterAttempt(String name, String phoneNumber, String companyName, String password, String passwordRepeat, boolean userAgreementAccepted, boolean dataProtectionPermissionAccepted, Device device) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.userAgreementAccepted = userAgreementAccepted;
        this.dataProtectionPermisionAccepted = dataProtectionPermissionAccepted;
        this.device = device;
    }
}
