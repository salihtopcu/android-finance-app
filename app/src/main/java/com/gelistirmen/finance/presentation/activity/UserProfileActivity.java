package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.DeleteBankAccountDao;
import com.gelistirmen.finance.data.remote.membership.UserProfileDao;
import com.gelistirmen.finance.model.membership.BankAccount;
import com.gelistirmen.finance.presentation.view.BankAccountViewHandler;
import com.gelistirmen.finance.presentation.view.PropertyViewHandler;
import com.gelistirmen.finance.util.Method;

import java.util.HashMap;

import butterknife.BindView;

public class UserProfileActivity extends BaseActivity {

    public static boolean requiresRefresh = false;

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.avatarTextView) TextView avatarTextView;
    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.bankAccountsTitleTextView) TextView bankAccountsTitleTextView;
    @BindView(R.id.profileInfoContentLinearLayout) LinearLayout profileInfoContentLinearLayout;
    @BindView(R.id.bankAccountsContentLinearLayout) LinearLayout bankAccountsContentLinearLayout;
    @BindView(R.id.companyInfoContentLinearLayout) LinearLayout companyInfoContentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        super.showActionBarBackButton();
        super.hideActionBarImage();
        super.hideBackgroundImage();
        super.setMainScrollContent(this.scrollView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requiresRefresh)
            this.loadContent();
    }

    @Override
    public void loadContent() {
        super.showProgressDialog();
        super.loadUserProfile();
        if (!MyApplication.mocking)
            this.scrollView.setVisibility(View.INVISIBLE);
    }

    private void displayUserProfile() {
        String avatarText = "";
        for (String word: MyApplication.userProfile.name.split(" ")) {
            if (word.trim().length() > 0)
                avatarText += word.substring(0, 1);
        }
        this.avatarTextView.setText(avatarText);

        this.nameTextView.setText(MyApplication.userProfile.name);

        this.profileInfoContentLinearLayout.removeAllViews();
        this.profileInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.phone_number), MyApplication.userProfile.phoneNumber).getView());
//        profileInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.email), "").getView());

        this.bankAccountsContentLinearLayout.removeAllViews();
        if (MyApplication.userProfile.company.bankAccounts.size() > 0) {
            for (final BankAccount bankAccount : MyApplication.userProfile.company.bankAccounts) {
                this.bankAccountsContentLinearLayout.addView(new BankAccountViewHandler(this, bankAccount,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                showToastMessage("Bank Account tapped");
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, Object> params = new HashMap<>();
                                params.put(BankAccountActivity.BANK_ACCOUNT_ID_KEY, bankAccount.id);
                                startActivity(BankAccountActivity.class, params);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showConfirmDialog(getString(R.string.delete_bank_account_confirm_message), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteBankAccount(bankAccount);
                                    }
                                });
                            }
                        }
                ).getView());
            }
            this.bankAccountsTitleTextView.setVisibility(View.VISIBLE);
        } else
            this.bankAccountsTitleTextView.setVisibility(View.GONE);

        this.companyInfoContentLinearLayout.removeAllViews();
        this.companyInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.company_name), MyApplication.userProfile.company.name).getView());
        this.companyInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.tax_no), MyApplication.userProfile.company.taxNo).getView());
        this.companyInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.tax_office), MyApplication.userProfile.company.taxOffice).getView());
        this.companyInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.address), MyApplication.userProfile.company.address).getView());
        this.companyInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.phone_number), MyApplication.userProfile.company.phoneNumber).getView());
        this.companyInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.email), MyApplication.userProfile.company.email).getView());
    }

    private void deleteBankAccount(BankAccount bankAccount) {
        super.showProgressDialog();
        new DeleteBankAccountDao(this, bankAccount.id).execute();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.daoDidSuccess(dao, data);
        if (dao instanceof UserProfileDao) {
            this.displayUserProfile();
            super.hideProgressDialog();
            Method.fadeIn(this.scrollView);
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        if (dao instanceof DeleteBankAccountDao) {
            this.loadContent();
        }
    }
}
