package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.BankAccountDao;
import com.gelistirmen.finance.data.remote.membership.UpdateBankAccountDao;
import com.gelistirmen.finance.model.membership.BankAccount;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FMEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class BankAccountActivity extends BaseActivity {

    public static final String BANK_ACCOUNT_ID_KEY = "BANK_ACCOUNT";

    @BindView(R.id.bankNameEditText) FMEditText bankNameEditText;
    @BindView(R.id.branchCodeEditText) FMEditText branchCodeEditText;
    @BindView(R.id.accountNoEditText) FMEditText accountNoEditText;
    @BindView(R.id.ibanEditText) FMEditText ibanEditText;

    String bankAccountId;
    BankAccount bankAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bankAccountId = super.getIntent().getStringExtra(BANK_ACCOUNT_ID_KEY);
        setContentView(R.layout.activity_bank_account);
        super.setActionBarTitle(R.string.bank_account);
        super.showActionBarBackButton();
        super.hideBackgroundImage();
    }

    @Override
    public void loadContent() {
        new BankAccountDao(this, this.bankAccountId).execute();
        super.showProgressDialog();
    }

    private void displayBankAccount() {
        this.bankNameEditText.setText(this.bankAccount.bankName);
        this.branchCodeEditText.setText(this.bankAccount.branchCode);
        this.accountNoEditText.setText(this.bankAccount.accountNo);
        this.ibanEditText.setText(this.bankAccount.iban.replace(" ", ""));
    }

    @OnClick(R.id.saveButton)
    public void saveButtonAction(FMButton button) {
        this.bankAccount.bankName = this.bankNameEditText.getTextAsString().trim();
        this.bankAccount.branchCode = this.branchCodeEditText.getTextAsString().trim();
        this.bankAccount.accountNo = this.accountNoEditText.getTextAsString().trim();
        this.bankAccount.iban = this.ibanEditText.getText().toString();
        new UpdateBankAccountDao(this, this.bankAccount).execute();
        super.showProgressDialog();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        if (dao instanceof BankAccountDao) {
            this.bankAccount = (BankAccount) data;
            this.displayBankAccount();
            super.hideProgressDialog();
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        if (dao instanceof UpdateBankAccountDao) {
            super.hideProgressDialog();
            MyApplication.messageForActivity = getString(R.string.bank_account_saved_message);
            UserProfileActivity.requiresRefresh = true;
            super.finish();
        }
    }
}
