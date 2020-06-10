package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ScrollView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.ChangePasswordDao;
import com.gelistirmen.finance.model.membership.PasswordChangeAttempt;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FMEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.currentPasswordEditText) FMEditText currentPasswordEditText;
    @BindView(R.id.newPasswordEditText) FMEditText newPasswordEditText;
    @BindView(R.id.passwordRepeatEditText) FMEditText passwordRepeatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        super.showActionBarBackButton();
        super.setMainScrollContent(this.scrollView);
    }

    @Override
    public void onBackPressed() {
        if (Cache.getRequiresPasswordChange())
            Cache.setAccessToken("");
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && Cache.getRequiresPasswordChange())
            Cache.setAccessToken("");
        return super.onOptionsItemSelected(item);
    }

    private boolean validatePasswordChangeAttempt(PasswordChangeAttempt passwordChangeAttempt) {
        boolean result = false;
        if (passwordChangeAttempt.currentPassword.isEmpty())
            super.showErrorDialog(getResources().getString(R.string.current_password_not_exists_warning));
        else if (passwordChangeAttempt.newPassword.isEmpty())
            super.showErrorDialog(getResources().getString(R.string.new_password_not_exists_warning));
        else if (passwordChangeAttempt.newPassword.length() < 6)
            super.showErrorDialog(getResources().getString(R.string.password_length_warning));
        else if (!passwordChangeAttempt.confirmPassword.equals(passwordChangeAttempt.newPassword))
            super.showErrorDialog(getResources().getString(R.string.password_repeat_warning));
        else
            result = true;
        return result;
    }

    @OnClick(R.id.continueButton)
    public void continueButtonAction(FMButton button) {
        PasswordChangeAttempt passwordChangeAttempt = new PasswordChangeAttempt(Cache.getUserId(),
                this.currentPasswordEditText.getTextAsString(),
                this.newPasswordEditText.getTextAsString(),
                this.passwordRepeatEditText.getTextAsString());
        if (this.validatePasswordChangeAttempt(passwordChangeAttempt)) {
            super.showProgressDialog();
            new ChangePasswordDao(this, passwordChangeAttempt).execute();
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        super.hideProgressDialog();
        super.showToastMessage(R.string.change_password_success_message);
        Cache.setRequiresPasswordChange(false);
        finish();
    }
}
