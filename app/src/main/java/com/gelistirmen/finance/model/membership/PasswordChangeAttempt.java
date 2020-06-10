package com.gelistirmen.finance.model.membership;

public class PasswordChangeAttempt {
    public String userId;
    public String currentPassword;
    public String newPassword;
    public String confirmPassword;

    public PasswordChangeAttempt(String userId, String currentPassword, String newPassword, String confirmPassword) {
        this.userId = userId;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
