package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.GeneratePayeeContractDao;
import com.gelistirmen.finance.model.membership.BankAccount;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.presentation.view.BankAccountViewHandler;
import com.gelistirmen.finance.presentation.view.FMEditText;
import com.gelistirmen.finance.presentation.view.FMRadioButton;
import com.gelistirmen.finance.presentation.view.PropertyViewHandler;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class EftOrderActivity extends BaseActivity implements BankAccountViewHandler.Listener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.paymentInfoContentLinearLayout) LinearLayout paymentInfoContentLinearLayout;
    @BindView(R.id.accountInfoContentLinearLayout) LinearLayout accountInfoContentLinearLayout;
    @BindView(R.id.newAccountRadioButton) FMRadioButton newAccountRadioButton;
    @BindView(R.id.newAccountAreaLinearLayout) LinearLayout newAccountAreaLinearLayout;
    @BindView(R.id.bankNameEditText) FMEditText bankNameEditText;
    @BindView(R.id.branchCodeEditText) FMEditText branchCodeEditText;
    @BindView(R.id.ibanRadioButton) FMRadioButton ibanRadioButton;
    @BindView(R.id.accountNoRadioButton) FMRadioButton accountNoRadioButton;
    @BindView(R.id.ibanEditText) FMEditText ibanEditText;
    @BindView(R.id.accountNoEditText) FMEditText accountNoEditText;

    ArrayList<FMRadioButton> bankAccountRadioButtons = new ArrayList<>();

    Quote quote = QuoteAssessmentActivity.quote;
    BankAccount.List bankAccounts;
    BankAccount selectedBankAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eft_order);
        super.setActionBarTitle(R.string.eft_order);
        super.hideBackgroundImage();
        super.showActionBarBackButton();
        super.setMainScrollContent(this.scrollView);

        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.amount), "15.000,00").getView());

        this.bankAccounts = MyApplication.userProfile.company.bankAccounts;
        this.accountInfoContentLinearLayout.removeAllViews();
        for (BankAccount bankAccount: this.bankAccounts) {
            BankAccountViewHandler bankAccountViewHandler = new BankAccountViewHandler(this, bankAccount, true, this);
            this.accountInfoContentLinearLayout.addView(bankAccountViewHandler.getView());
            this.bankAccountRadioButtons.add(bankAccountViewHandler.getRadioButton());
        }
        this.accountInfoContentLinearLayout.addView(this.newAccountRadioButton);
        this.bankAccountRadioButtons.add(this.newAccountRadioButton);
        this.newAccountRadioButton.setOnCheckedChangeListener(this);
        this.ibanRadioButton.setOnCheckedChangeListener(this);
        this.accountNoRadioButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(BankAccountViewHandler bankAccountViewHandler, BankAccount bankAccount, boolean isChecked) {
        if (isChecked) {
            for (FMRadioButton radioButton: this.bankAccountRadioButtons)
                radioButton.setCheckedWithoutListener(bankAccountViewHandler.getRadioButton() == radioButton);
            newAccountAreaLinearLayout.setVisibility(View.GONE);
            selectedBankAccount = bankAccount;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == this.newAccountRadioButton) {
            if (isChecked) {
                for (FMRadioButton radioButton : this.bankAccountRadioButtons)
                    radioButton.setCheckedWithoutListener(buttonView == radioButton);
                selectedBankAccount = null;
            }
            newAccountAreaLinearLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        } else if (buttonView == this.ibanRadioButton || buttonView == this.accountNoRadioButton) {
            if (isChecked) {
                this.ibanEditText.setVisibility(buttonView == this.ibanRadioButton ? View.VISIBLE : View.GONE);
                this.accountNoEditText.setVisibility(buttonView == this.accountNoRadioButton ? View.VISIBLE : View.GONE);
            }
        }
    }

    @OnClick(R.id.continueButton)
    public void continueButtonAction() {
        super.showProgressDialog();
        if (this.newAccountRadioButton.isChecked()) {
            BankAccount newBankAccount = new BankAccount(
                    this.bankNameEditText.getTextAsString().trim(),
                    this.branchCodeEditText.getTextAsString().trim(),
                    this.accountNoRadioButton.isChecked() ? this.accountNoEditText.getTextAsString().trim() : "",
                    this.ibanRadioButton.isChecked() ? this.ibanEditText.getTextAsString().trim() : "");
//            new CompleteQuoteByPayeeDao(this, this.quote, newBankAccount).execute();
            new GeneratePayeeContractDao(this, this.quote, newBankAccount).execute();
        } else {
//            new CompleteQuoteByPayeeDao(this, this.quote, this.selectedBankAccount.id).execute();
            new GeneratePayeeContractDao(this, this.quote, this.selectedBankAccount.id).execute();
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        if (dao instanceof  GeneratePayeeContractDao) {
            super.hideProgressDialog();
    //        HomeActivity.requiresRefresh = true;
            HashMap<String, Object> params = new HashMap<>();
            params.put(ContractActivity.QUOTE_ID_KEY, this.quote.id);
            params.put(ContractActivity.CONTRACT_TYPE_KEY, ContractActivity.CONTRACT_TYPE_PAYEE);
            startActivity(ContractActivity.class, params);
            super.finish();
        }
    }
}
