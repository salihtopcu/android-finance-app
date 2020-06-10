package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gelistirmen.finance.BuildConfig;
import com.gelistirmen.finance.R;

import butterknife.BindView;

public class AboutApplicationActivity extends BaseActivity {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.versionInfoTextView) TextView versionInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);
        super.showActionBarBackButton();
        super.hideActionBarImage();
        super.hideBackgroundImage();
        super.setMainScrollContent(this.scrollView);
        this.versionInfoTextView.setVisibility(View.VISIBLE);
        this.versionInfoTextView.setText("V " + BuildConfig.VERSION_NAME);
    }
}
