package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.widget.ScrollView;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.RegisterDao;
import com.gelistirmen.finance.model.membership.Device;
import com.gelistirmen.finance.model.membership.RegisterAttempt;
import com.gelistirmen.finance.model.membership.RegisterResponse;
import com.gelistirmen.finance.presentation.dialog.SecurityCodeDialog;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FMCheckBox;
import com.gelistirmen.finance.presentation.view.FMEditText;
import com.gelistirmen.finance.util.Method;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.nameEditText) FMEditText nameEditText;
    @BindView(R.id.phoneNumberEditText) FMEditText phoneNumberEditText;
    @BindView(R.id.companyNameEditText) FMEditText companyNameEditText;
    @BindView(R.id.passwordEditText) FMEditText passwordEditText;
    @BindView(R.id.passwordRepeatEditText) FMEditText passwordRepeatEditText;
    @BindView(R.id.userAgreementCheckbox) FMCheckBox userAgreementCheckbox;
    @BindView(R.id.permissionCheckbox) FMCheckBox permissionCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        super.showActionBarBackButton();
        super.hideActionBarImage();

        super.setMainScrollContent(this.scrollView);

        if (MyApplication.developmentMode) {
            this.nameEditText.setText("Name Last Name");
            this.phoneNumberEditText.setText("5555022467");
            this.companyNameEditText.setText("Company");
            this.passwordEditText.setText("aA1234");
            this.passwordRepeatEditText.setText("aA1234");
            this.userAgreementCheckbox.setChecked(true);
            this.permissionCheckbox.setChecked(true);
        }
    }

    @OnClick(R.id.registerButton)
    public void registerButtonAction(FMButton button) {
        Device device = new Device(
                Method.getDeviceManufacturer(),
                Method.getDeviceModel(),
                "Android",
                Method.getAndroidVersionName(),
                Method.getDeviceId());
        RegisterAttempt registerAttempt = new RegisterAttempt(
                this.nameEditText.getTextAsString().trim(),
                this.phoneNumberEditText.getTextAsString(),
                this.companyNameEditText.getTextAsString(),
                this.passwordEditText.getTextAsString(),
                this.passwordRepeatEditText.getTextAsString(),
                this.userAgreementCheckbox.isChecked(),
                this.permissionCheckbox.isChecked(),
                device);
        if (this.validateRegisterAttempt(registerAttempt)) {
            super.showProgressDialog();
            new RegisterDao(this, registerAttempt).execute();
        }
    }

    @OnClick(R.id.userAgreementButton)
    public void userAgreementButtonAction(FMButton button) {
        super.showToastMessage("User agreement will be displayed.");
    }

    private boolean validateRegisterAttempt(RegisterAttempt registerAttempt) {
        boolean result = false;
        if (registerAttempt.name.replace(" ", "").length() == registerAttempt.name.length())
            super.showErrorDialog(getResources().getString(R.string.full_name_warning));
        else if (registerAttempt.phoneNumber.length() < 10)
            super.showErrorDialog(getResources().getString(R.string.phone_number_length_warning));
        else if (registerAttempt.companyName.isEmpty())
            super.showErrorDialog(getResources().getString(R.string.company_name_warning));
        else if (registerAttempt.companyName.length() < 3)
            super.showErrorDialog(getResources().getString(R.string.company_name_length_warning));
        else if (registerAttempt.password.length() == 0)
            super.showErrorDialog(getResources().getString(R.string.password_not_exists_warning));
        else if (registerAttempt.password.length() < 6)
            super.showErrorDialog(getResources().getString(R.string.password_length_warning));
        else if (!registerAttempt.password.equals(registerAttempt.passwordRepeat))
            super.showErrorDialog(getResources().getString(R.string.password_repeat_warning));
        else if (!registerAttempt.userAgreementAccepted)
            super.showErrorDialog(getResources().getString(R.string.user_agreement_warning));
        else if (!registerAttempt.dataProtectionPermisionAccepted)
            super.showErrorDialog(getString(R.string.data_protection_permission_warning));
        else
            result = true;
        return result;
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        if (dao instanceof RegisterDao) {
            super.hideProgressDialog();
            RegisterResponse registerResponse = (RegisterResponse) data;
            Cache.setUserId(registerResponse.userId);
            Cache.setRequiresPasswordChange(registerResponse.changePasswordRequest);
            RegisterAttempt registerAttempt = ((RegisterDao) dao).registerAttempt;
            super.showSecurityCodeDialog(new SecurityCodeDialog.Listener() {
                @Override
                public void verificationDidSuccess() {
                    MyApplication.messageForActivity = "Phone number verified.";
                    finish();
                }
            }, registerAttempt.phoneNumber, true);
            Cache.setShowMobileSignatureInfo(true);
        }
    }
}
