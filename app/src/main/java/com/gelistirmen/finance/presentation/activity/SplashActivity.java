package com.gelistirmen.finance.presentation.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.UserProfileDao;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.util.Method;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    private final int CAMERA_PERMISSION_CODE = 1;

    @BindView(R.id.retryButton) FMButton retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String cacheLanguage = Cache.getApplicationLanguage();
        if (Method.isNotNullOrEmpty(cacheLanguage))
            super.setLanguage(cacheLanguage);
        else {
            String deviceLanguage = Locale.getDefault().getLanguage();
            if (deviceLanguage.equals(Constants.LanguageCode.Turkish))
                super.setLanguage(deviceLanguage);
            else
                super.setLanguage(Constants.LanguageCode.English);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        super.hideActionBar();
        super.hideBackgroundImage();
//        com.crashlytics.android.Crashlytics.getInstance().crash();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.checkPermissions();
    }

    private void checkPermissions() {
        boolean allPermissionsGranted = false;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA }, CAMERA_PERMISSION_CODE);
        else
            allPermissionsGranted = true;

        if (allPermissionsGranted) {
            if (MyApplication.mocking) {
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            doYourJob();
                        }
                    }
                };
                thread.start();
            } else
                this.doYourJob();
        }
    }

    private void doYourJob() {
        if (Method.isNullOrEmpty(Cache.getAccessToken()))
            super.startActivityAsRoot(PreLoginActivity.class);
        else if (Cache.getRequiresPasswordChange())
            super.startActivity(ChangePasswordActivity.class);
        else
            super.loadUserProfile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                super.showToastMessage("Camera permission granted");
                this.checkPermissions();
            } else {
                super.showInfoDialog("Please allow Finance App to use your device's camera via Android Application Manager.", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                super.showToastMessage("Camera permission denied");
            }
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.daoDidSuccess(dao, data);
        if (dao instanceof UserProfileDao)
            super.startActivityAsRoot(HomeActivity.class);
    }

    @Override
    public void daoDidFail(VolleyDao dao, VolleyDao.Error error) {
        super.daoDidFail(dao, error);
        if (error.type != VolleyDao.ErrorType.InvalidToken)
            this.retryButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.retryButton)
    public void retryButtonAction(FMButton button) {
        this.retryButton.setVisibility(View.GONE);
        this.doYourJob();
    }
}
