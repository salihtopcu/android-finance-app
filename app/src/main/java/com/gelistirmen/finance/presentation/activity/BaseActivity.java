package com.gelistirmen.finance.presentation.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.UserProfileDao;
import com.gelistirmen.finance.model.membership.UserProfile;
import com.gelistirmen.finance.presentation.dialog.ConfirmDialog;
import com.gelistirmen.finance.presentation.dialog.SecurityCodeDialog;
import com.gelistirmen.finance.presentation.dialog.SimpleDialog;
import com.gelistirmen.finance.util.Method;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements VolleyDao.VolleyDaoListener, ViewTreeObserver.OnScrollChangedListener {

    private Toolbar toolbar;

    private ConstraintLayout actionBarContainerView;
    private View actionBarBackgroundView;
    private ImageView actionBarImageView;
    private TextView actionBarTextView;

    private ProgressDialog progressDialog;
    private SimpleDialog infoDialog;
    private SimpleDialog errorDialog;
    private ConfirmDialog confirmDialog;

    private View mainScrollContent;

    public void setMainScrollContent(View view) {
        this.mainScrollContent = view;
        this.mainScrollContent.getViewTreeObserver().addOnScrollChangedListener(this);
    }

    protected boolean isFirstLoad = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.isFirstLoad)
            this.loadContent();
        if (Method.isNotNullOrEmpty(MyApplication.messageForActivity)) {
            this.showInfoDialog(MyApplication.messageForActivity);
            MyApplication.messageForActivity = "";
        }
    }

    public void loadContent() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isFirstLoad = false;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.master_layout);
        FrameLayout contentLayout = findViewById(R.id.contentLayout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutResID, null);
        contentLayout.addView(view);

        ButterKnife.bind(this);

        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.actionBarContainerView = findViewById(R.id.actionBarContainerView);
        this.actionBarBackgroundView = findViewById(R.id.actionBarBackgroundView);
        this.actionBarTextView = findViewById(R.id.actionBarTextView);
        this.actionBarImageView = findViewById(R.id.actionBarImageView);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    protected void hideActionBar() {
        getSupportActionBar().hide();
        this.actionBarContainerView.setVisibility(View.GONE);
    }

    public void showActionBarBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void showNavigationMenu() {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navView);
        if (MyApplication.userProfile != null) {
            ((TextView) navigationView.findViewById(R.id.userNameTextView)).setText(MyApplication.userProfile.name);
            navigationView.findViewById(R.id.userInfoLinearLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(UserProfileActivity.class);
                    drawer.closeDrawer(Gravity.LEFT, true);
                }
            });

            String avatarText = "";
            for (String word : MyApplication.userProfile.name.split(" ")) {
                if (word.trim().length() > 0)
                    avatarText += word.substring(0, 1);
            }
            ((TextView) navigationView.findViewById(R.id.avatarTextView)).setText(avatarText);
        }
        ListView navigationMenuListView = navigationView.findViewById(R.id.navigationMenuListView);
        final NavMenuButton[] navMenuButtons = {
                new NavMenuButton(getString(R.string.add_invoice), R.drawable.ic_add_invoice_white),
                new NavMenuButton(getString(R.string.upload_document), R.drawable.ic_add_document_white),
//                new NavMenuButton(getString(R.string.notifications), R.drawable.ic_bell_white),
                new NavMenuButton(getString(R.string.settings), R.drawable.ic_settings_white)
//                new NavMenuButton(getString(R.string.online_operations), R.drawable.ic_online_processes),
        };
        navigationMenuListView.setAdapter(new NavMenuAdapter(navMenuButtons));
        navigationMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(UploadInvoiceActivity.class);
                        break;
                    case 1:
                        startActivity(UploadDocumentActivity.class);
                        break;
//                    case 2:
//                        startActivity(NotificationsActivity.class);
//                        break;
                    case 2:
                        startActivity(SettingsActivity.class);
                        break;
//                    case 1: startActivity(OnlineOperationsActivity.class); break;
                }
                drawer.closeDrawer(Gravity.LEFT, true);
            }
        });
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onScrollChanged() {
        this.actionBarBackgroundView.setAlpha(Math.min((float) this.mainScrollContent.getScrollY() / 400, 0.5f));
        this.actionBarBackgroundView.setElevation(this.mainScrollContent.getScrollY() > 0 ? 5 : 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return false;
        } else
            return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        this.actionBarTextView.setText(title);
        this.actionBarTextView.setVisibility(View.VISIBLE);
        this.hideActionBarImage();
    }

    public void setActionBarTitle(@StringRes int id) {
        this.setActionBarTitle(getString(id));
    }

    public void hideBackgroundImage() {
//        findViewById(R.id.backgroundImageView).setVisibility(View.GONE);
    }

    public void hideActionBarImage() {
        this.actionBarImageView.setVisibility(View.GONE);
    }

    public void showProgressDialog(String message) {
        this.progressDialog = ProgressDialog.show(this, "", message, false, false);
    }

    public void showProgressDialog() {
        if (this.progressDialog == null || (this.progressDialog != null && !this.progressDialog.isShowing()))
            this.showProgressDialog(getResources().getString(R.string.loading));
    }

    public void hideProgressDialog() {
        if (this.progressDialog != null && this.progressDialog.isShowing())
            this.progressDialog.dismiss();
    }

    public void showInfoDialog(String message, @Nullable View.OnClickListener buttonOnClickListener) {
        if (this.infoDialog == null) {
            this.infoDialog = new SimpleDialog(this, SimpleDialog.Type.Info);
            this.infoDialog.setTitle(getResources().getString(R.string.info));
        }
        this.infoDialog.setContent(message);
        this.infoDialog.setButtonOnClickListener(buttonOnClickListener);
        this.infoDialog.show();
    }

    public void showInfoDialog(String message) {
        this.showInfoDialog(message, null);
    }

    public void showErrorDialog(String message) {
        if (this.errorDialog == null) {
            this.errorDialog = new SimpleDialog(this, SimpleDialog.Type.Error);
            this.errorDialog.setTitle(getResources().getString(R.string.error));
        }
        this.errorDialog.setContent(message);
        this.errorDialog.show();
    }

    public void showErrorDialog(String message, @Nullable View.OnClickListener buttonOnClickListener, boolean cancelable) {
        if (this.errorDialog == null) {
            this.errorDialog = new SimpleDialog(this, SimpleDialog.Type.Error);
            this.errorDialog.setTitle(getResources().getString(R.string.error));
            this.errorDialog.setCancelable(cancelable);
        }
        this.errorDialog.setContent(message);
        this.errorDialog.setButtonOnClickListener(buttonOnClickListener);
        this.errorDialog.show();
    }

    public void showConfirmDialog(String message, View.OnClickListener confirmButtonOnClickListener, @Nullable View.OnClickListener cancelButtonOnClickListener) {
        if (this.confirmDialog == null) {
            this.confirmDialog = new ConfirmDialog(this);
        }
        this.confirmDialog.setContent(message);
        this.confirmDialog.setConfirmButtonOnClickListener(confirmButtonOnClickListener);
        this.confirmDialog.setCancelButtonOnClickListener(cancelButtonOnClickListener);
        this.confirmDialog.show();
    }

    public void showConfirmDialog(String message, View.OnClickListener confirmButtonOnClickListener) {
        this.showConfirmDialog(message, confirmButtonOnClickListener, null);
    }

    public void showSecurityCodeDialog(SecurityCodeDialog.Listener listener, String phoneNumber, boolean showTimer) {
        SecurityCodeDialog securityCodeDialog = new SecurityCodeDialog(this, listener, phoneNumber);
        securityCodeDialog.show(showTimer);
    }

    private void startActivity(Class<?> cls, boolean asRoot, @Nullable HashMap<String, Object> params) {
        Intent intent = new Intent(this, cls);
        if (asRoot)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (params != null) {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String key = (String) pair.getKey();
                Object value = pair.getValue();
                if (value instanceof String)
                    intent.putExtra(key, (String) value);
                else if (value instanceof Integer)
                    intent.putExtra(key, (Integer) value);
                else if (value instanceof Double)
                    intent.putExtra(key, (Double) value);
                else if (value instanceof Boolean)
                    intent.putExtra(key, (Boolean) value);
                else if (value instanceof Serializable)
                    intent.putExtra(key, (Serializable) value);
                it.remove();
            }
        }
        startActivity(intent);
    }

    public void startActivity(Class<?> cls) {
        this.startActivity(cls, false, null);
    }

    public void startActivityAsRoot(Class<?> cls) {
        this.startActivity(cls, true, null);
    }

    public void startActivity(Class<?> cls, HashMap<String, Object> params) {
        this.startActivity(cls, false, params);
    }

    public void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToastMessage(int stringId) {
        this.showToastMessage(getResources().getString(stringId));
    }

    public void setLanguage(String code) {
        Method.setApplicationLanguage(this, code);
        Cache.setApplicationLanguage(code);
    }

    public String getLanguage() {
        return Cache.getApplicationLanguage();
    }

    public void loadUserProfile() {
        new UserProfileDao(this).execute();
    }

    public void logout() {
        Cache.setAccessToken("");
        Cache.setUserId("");
        Cache.setRequiresPasswordChange(false);
        startActivityAsRoot(PreLoginActivity.class);
    }

    // VolleyDaoListener Methods

    public void daoDidSuccess(VolleyDao dao) {
    }

    public void daoDidSuccess(VolleyDao dao, Object data) {
        if (dao instanceof UserProfileDao)
            MyApplication.userProfile = (UserProfile) data;
    }

    public void daoDidFail(VolleyDao dao, VolleyDao.Error error) {
        this.hideProgressDialog();
        if (error.type == VolleyDao.ErrorType.InvalidToken) {
            Cache.setAccessToken("");
            Cache.setUserId("");
            Cache.setRequiresPasswordChange(false);
            this.startActivityAsRoot(PreLoginActivity.class);
        } else
            this.showErrorDialog(error.message);
    }

    private class NavMenuButton {
        String title;
        int iconId;

        NavMenuButton(String title, int iconId) {
            this.title = title;
            this.iconId = iconId;
        }
    }

    private class NavMenuAdapter extends ArrayAdapter<NavMenuButton> {

        public NavMenuAdapter(@NonNull NavMenuButton[] buttons) {
            super(BaseActivity.this, R.layout.nav_menu_button, buttons);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = LayoutInflater.from(BaseActivity.this).inflate(R.layout.nav_menu_button, parent, false);
            ImageView iconImageView = convertView.findViewById(R.id.iconImageView);
            TextView titleTextView = convertView.findViewById(R.id.titleTextView);
            NavMenuButton button = super.getItem(position);
            iconImageView.setImageResource(button.iconId);
            titleTextView.setText(button.title);

            return convertView;
        }
    }
}
