package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ScrollView;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.LoginDao;
import com.gelistirmen.finance.model.membership.Device;
import com.gelistirmen.finance.model.membership.LoginAttempt;
import com.gelistirmen.finance.model.membership.LoginResponse;
import com.gelistirmen.finance.presentation.dialog.SecurityCodeDialog;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FMCheckBox;
import com.gelistirmen.finance.presentation.view.FMEditText;
import com.gelistirmen.finance.util.Method;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.phoneNumberEditText) FMEditText phoneNumberEditText;
    @BindView(R.id.passwordEditText) FMEditText passwordEditText;
    @BindView(R.id.rememberMeCheckBox) FMCheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        super.showActionBarBackButton();
        super.hideActionBarImage();
        super.setMainScrollContent(this.scrollView);

        if (!TextUtils.isEmpty(Cache.getUserPhoneNumber())) {
            phoneNumberEditText.setText(Cache.getUserPhoneNumber());
            this.rememberMeCheckBox.setChecked(true);
        }

        if (MyApplication.developmentMode) {
            this.phoneNumberEditText.setText("5555022467");
            this.passwordEditText.setText("Aa1234");
        }
    }

    @OnClick(R.id.loginButton)
    public void loginButtonAction(FMButton button) {
        Device device = new Device(
                Method.getDeviceManufacturer(),
                Method.getDeviceModel(),
                "Android",
                Method.getAndroidVersionName(),
                Method.getDeviceId());
        LoginAttempt loginAttempt = new LoginAttempt(
                this.phoneNumberEditText.getTextAsString(),
                this.passwordEditText.getTextAsString(),
                device);
        if (this.validateLoginAttempt(loginAttempt)) {
            super.showProgressDialog();
            new LoginDao(this, loginAttempt).execute();
        }
    }

    @OnClick(R.id.forgotPasswordButton)
    public void forgotPasswordButtonAction(FMButton button) {
        super.startActivity(ForgotPasswordActivity.class);
    }

    private boolean validateLoginAttempt(LoginAttempt loginAttempt) {
        boolean result = false;
        if (loginAttempt.phoneNumber.length() < 10)
            super.showErrorDialog(getResources().getString(R.string.phone_number_length_warning));
        else if (loginAttempt.password.length() == 0)
            super.showErrorDialog(getResources().getString(R.string.password_not_exists_warning));
        else
            result = true;
        return result;
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.hideProgressDialog();
        LoginResponse loginResponse = (LoginResponse) data;
        Cache.setAccessToken(loginResponse.token);
        Cache.setRefreshToken(loginResponse.refreshToken);
        Cache.setUserId(loginResponse.userId);
        Cache.setRequiresPasswordChange(loginResponse.changePasswordRequest);
        if (this.rememberMeCheckBox.isChecked())
            Cache.setUserPhoneNumber(phoneNumberEditText.getTextAsString());
        else
            Cache.setUserPhoneNumber("");
        super.startActivityAsRoot(SplashActivity.class);
    }

    @Override
    public void daoDidFail(VolleyDao dao, VolleyDao.Error error) {
        if (dao instanceof LoginDao && error.type == VolleyDao.ErrorType.Response_BadRequest) {
            LoginAttempt loginAttempt = ((LoginDao) dao).loginAttempt;
            super.hideProgressDialog();
            super.showSecurityCodeDialog(new SecurityCodeDialog.Listener() {
                @Override
                public void verificationDidSuccess() {
                    LoginActivity.super.showInfoDialog("Phone number verified. You can try to login again.");
                }
            }, loginAttempt.phoneNumber, false);
        } else
            super.daoDidFail(dao, error);
    }
}
