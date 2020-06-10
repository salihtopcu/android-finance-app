package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.CompanyUpdateDao;
import com.gelistirmen.finance.data.remote.membership.UserProfileDao;
import com.gelistirmen.finance.data.remote.settings.CitiesDao;
import com.gelistirmen.finance.data.remote.settings.CountiesDao;
import com.gelistirmen.finance.model.membership.Company;
import com.gelistirmen.finance.model.settings.City;
import com.gelistirmen.finance.model.settings.County;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FMEditText;
import com.gelistirmen.finance.util.Method;

import butterknife.BindView;
import butterknife.OnClick;

public class CompanyProfileActivity extends BaseActivity {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.taxNoEditText) FMEditText taxNoEditText;
    @BindView(R.id.taxOfficeEditText) FMEditText taxOfficeEditText;
    @BindView(R.id.companyNameEditText) FMEditText companyNameEditText;
    @BindView(R.id.cityEditText) FMEditText cityEditText;
    @BindView(R.id.countyEditText) FMEditText countyEditText;
    @BindView(R.id.addressEditText) FMEditText addressEditText;
    @BindView(R.id.postCodeEditText) FMEditText postCodeEditText;
    @BindView(R.id.phoneNumberEditText) FMEditText phoneNumberEditText;
    @BindView(R.id.emailEditText) FMEditText emailEditText;
//    @BindView(R.id.webSiteEditText) FMEditText webSiteEditText;

    private MaterialDialog.Builder citiesDialogBuilder;
    private MaterialDialog.Builder countiesDialogBuilder;

    private City.List cities;
    private County.List counties;
    private boolean citiesLoaded = false;
    private boolean countiesLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_company_profile);

        super.showActionBarBackButton();
        super.hideBackgroundImage();
        super.setActionBarTitle(R.string.company_info);
        super.setMainScrollContent(this.scrollView);

        this.taxNoEditText.setText(MyApplication.userProfile.company.taxNo);
        this.taxOfficeEditText.setText(MyApplication.userProfile.company.taxOffice);
        this.companyNameEditText.setText(MyApplication.userProfile.company.name);
        this.addressEditText.setText(MyApplication.userProfile.company.address);
        this.postCodeEditText.setText(MyApplication.userProfile.company.postalCode);
        this.phoneNumberEditText.setText(MyApplication.userProfile.company.phoneNumber);
        this.emailEditText.setText(MyApplication.userProfile.company.email);
    }

    @Override
    public void loadContent() {
        if (MyApplication.userProfile.company.county != null) {
            this.setCityEditText(MyApplication.userProfile.company.county.city);
            this.setCountyEditText(MyApplication.userProfile.company.county);
            this.loadCounties(MyApplication.userProfile.company.county.city);
        } else
            this.countiesLoaded = true;
        this.loadCities();
    }

    private void loadCities() {
        super.showProgressDialog();
        new CitiesDao(this).execute();
    }

    private void loadCounties(City city) {
        super.showProgressDialog();
        this.countiesLoaded = false;
        new CountiesDao(this, city).execute();
    }

    private void setCityEditText(City city) {
        this.cityEditText.setText(city.name.toString());
        this.cityEditText.setTag(city);
    }

    private void setCountyEditText(County county) {
        if (county != null)
            this.countyEditText.setText(county.name);
        else
            this.countyEditText.setText("");
        this.countyEditText.setTag(county);
    }

    @OnClick(R.id.cityEditText)
    public void cityEditTextAction(FMEditText editText) {
        if (this.citiesDialogBuilder == null)
            this.citiesDialogBuilder = new MaterialDialog.Builder(this)
                    .title(R.string.city)
                    .negativeText(R.string.cancel);
        final CharSequence[] options = new CharSequence[this.cities.size()];
        for (int i = 0; i < this.cities.size(); i++)
            options[i] = this.cities.get(i).name;
        this.citiesDialogBuilder = this.citiesDialogBuilder.items(options)
            .itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                    setCityEditText(cities.get(which));
                    setCountyEditText(null);
                    new CountiesDao(CompanyProfileActivity.this, cities.get(which)).execute();
                    showProgressDialog();
                }
            });
        this.citiesDialogBuilder.show();
    }

    @OnClick(R.id.countyEditText)
    public void countyEditTextAction(FMEditText editText) {
        if (Method.isNotNullOrEmpty(this.cityEditText.getText().toString())) {
            if (this.countiesDialogBuilder == null)
                this.countiesDialogBuilder = new MaterialDialog.Builder(this)
                        .title(R.string.county)
                        .negativeText(R.string.cancel);
            final CharSequence[] options = new CharSequence[this.counties.size()];
            for (int i = 0; i < this.counties.size(); i++)
                options[i] = this.counties.get(i).name;
            this.countiesDialogBuilder = this.countiesDialogBuilder.items(options)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            setCountyEditText(counties.get(which));
                        }
                    });
            this.countiesDialogBuilder.show();
        }
    }

    @OnClick(R.id.saveButton)
    public void saveButtonAction(FMButton button) {
        Company dummyCompany = new Company(
                "",
                this.companyNameEditText.getTextAsString(),
                this.phoneNumberEditText.getTextAsString(),
                this.emailEditText.getTextAsString(),
                this.addressEditText.getTextAsString(),
                this.postCodeEditText.getTextAsString(),
                this.countyEditText.getTag() == null ? null : (County) this.countyEditText.getTag(),
                this.taxNoEditText.getTextAsString(),
                this.taxOfficeEditText.getTextAsString(),
                MyApplication.userProfile.company.webSite,
                null
        );
        if (this.validateCompany(dummyCompany)) {
            super.showProgressDialog();
            new CompanyUpdateDao(this, dummyCompany).execute();
        }
    }

    private boolean validateCompany(Company company) {
        return true;
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        if (dao instanceof CitiesDao || dao instanceof CountiesDao) {
            if (dao instanceof CitiesDao) {
                this.cities = (City.List) data;
                citiesLoaded = true;
            } else if (dao instanceof CountiesDao) {
                this.counties = (County.List) data;
                this.countiesLoaded = true;
            }
            if (this.citiesLoaded && this.countiesLoaded)
                super.hideProgressDialog();
        } else if (dao instanceof UserProfileDao) {
            super.hideProgressDialog();
            super.showInfoDialog(getString(R.string.company_profile_saved_successfully));
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        if (dao instanceof CompanyUpdateDao)
            super.loadUserProfile();
    }
}
