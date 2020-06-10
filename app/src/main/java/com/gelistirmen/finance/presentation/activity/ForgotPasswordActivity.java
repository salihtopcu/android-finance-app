package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.widget.ScrollView;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.ResetPasswordDao;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FMEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.phoneNumberEditText) FMEditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        super.showActionBarBackButton();
        super.setMainScrollContent(this.scrollView);
    }

    @OnClick(R.id.continueButton)
    public void continueButtonAction(FMButton button) {
        super.showProgressDialog();
        new ResetPasswordDao(this, phoneNumberEditText.getTextAsString()).execute();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.hideProgressDialog();
        MyApplication.messageForActivity = (String) data;
        super.finish();
    }
}
