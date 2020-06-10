package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.view.View;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.view.FMButton;

import butterknife.BindView;

public class PreLoginActivity extends BaseActivity {
    @BindView(R.id.loginButton) FMButton loginButton;
    @BindView(R.id.registerButton) FMButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_pre_login);
        super.hideActionBarImage();

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
            }
        });

        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.class);
            }
        });
    }
}
