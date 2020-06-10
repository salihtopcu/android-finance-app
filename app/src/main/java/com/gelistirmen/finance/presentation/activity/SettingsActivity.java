package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.settings.SettingsDao;
import com.gelistirmen.finance.model.settings.SettingsModel;
import com.gelistirmen.finance.presentation.view.FMButton;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.notificationsSwitch) SwitchCompat notificationsSwitch;
//    @BindView(R.id.languageButton) FMButton languageButton;
    @BindView(R.id.languageTextView) TextView languageTextView;

    SettingsModel settingsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        super.showActionBarBackButton();
        super.hideBackgroundImage();
        super.setActionBarTitle(R.string.settings);

        this.settingsModel = new SettingsModel(null, Cache.getApplicationLanguage());

        this.notificationsSwitch.setOnCheckedChangeListener(this);
        this.languageTextView.setText(super.getLanguage());
        super.showProgressDialog();
        new SettingsDao(this).execute();
    }

    @OnClick(R.id.companyProfileButton)
    public void companyProfileButtonAction(FMButton button) {
        super.startActivity(CompanyProfileActivity.class);
    }

    @OnClick(R.id.changePasswordButton)
    public void changePasswordProfileButtonAction(FMButton button) {
        super.startActivity(ChangePasswordActivity.class);
    }

    @OnClick(R.id.languageButton)
    public void languageButtonAction(FMButton button) {
        CharSequence[] options = { "Türkçe", "English" };
        final String[] codes = { Constants.LanguageCode.Turkish, Constants.LanguageCode.English };
        new MaterialDialog.Builder(this)
                .title(R.string.application_language)
                .items(options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (!getLanguage().equals(codes[which])) {
                            setLanguage(codes[which]);
                            settingsModel.language = codes[which];
                            new SettingsDao(null, settingsModel).execute();
                            SettingsActivity.super.startActivityAsRoot(SplashActivity.class);
                        }
                    }
                })
                .show();
    }

    @OnClick(R.id.aboutApplicationButton)
    public void aboutApplicationButtonAction(FMButton button) {
        super.startActivity(AboutApplicationActivity.class);
    }

    @OnClick(R.id.logoutButton)
    public void logoutButtonAction(FMButton button) {
        super.logout();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.settingsModel.allowNotification = isChecked;
        new SettingsDao(this, this.settingsModel).execute();
        super.showProgressDialog();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.hideProgressDialog();
        SettingsModel sm = ((SettingsModel) data);
        if (this.settingsModel.allowNotification == null) {
            this.notificationsSwitch.setOnCheckedChangeListener(null);
            this.notificationsSwitch.setChecked(sm.allowNotification);
            this.notificationsSwitch.setOnCheckedChangeListener(this);
        }
        this.settingsModel.allowNotification = sm.allowNotification;
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        super.hideProgressDialog();
    }
}
