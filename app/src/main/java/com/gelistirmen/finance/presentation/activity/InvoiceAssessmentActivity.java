package com.gelistirmen.finance.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.ApproveQuoteDebtorDao;
import com.gelistirmen.finance.data.remote.operation.ChangeQuoteDao;
import com.gelistirmen.finance.data.remote.operation.DebtorFingerPrintDao;
import com.gelistirmen.finance.data.remote.operation.DeclineQuoteDao;
import com.gelistirmen.finance.data.remote.operation.QuoteDao;
import com.gelistirmen.finance.model.operation.ChangeQuoteRequest;
import com.gelistirmen.finance.model.operation.DeclineQuoteRequest;
import com.gelistirmen.finance.model.operation.Fingerprint;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.presentation.dialog.DateNoteInputDialog;
import com.gelistirmen.finance.presentation.dialog.SimpleDialog;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.InvoiceCellViewHandler;
import com.gelistirmen.finance.presentation.view.PropertyViewHandler;
import com.gelistirmen.finance.util.Method;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class InvoiceAssessmentActivity extends BaseActivity implements DateNoteInputDialog.Listener {

    public static final String QUOTE_ID_KEY = "QUOTE_ID";
    public static final String QUOTE_STATUS_KEY = "QUOTE_STATUS";

    public static boolean contractAccepted = false;
    public static boolean signatureCompleted = false;

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.payeeInfoContentLinearLayout) LinearLayout payeeInfoContentLinearLayout;
    @BindView(R.id.paymentInfoContentLinearLayout) LinearLayout paymentInfoContentLinearLayout;
    @BindView(R.id.invoicesLinearLayout) LinearLayout invoicesLinearLayout;

    private String quoteId;
    private int quoteStatus;
    private Quote quote;
    private String fingerPrintID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quoteId = super.getIntent().getStringExtra(QUOTE_ID_KEY);
        this.quoteStatus = super.getIntent().getIntExtra(QUOTE_STATUS_KEY, 0);
        setContentView(R.layout.activity_invoice_assessment);
        super.showActionBarBackButton();
        super.setActionBarTitle(R.string.invoice_approval);
        super.hideBackgroundImage();
        super.setMainScrollContent(this.scrollView);
        if (!MyApplication.mocking)
            this.scrollView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!super.isFirstLoad) {
            if (contractAccepted) {
                super.showProgressDialog();
                new DebtorFingerPrintDao(this, this.quoteId).execute();
                contractAccepted = false;
            } else if (signatureCompleted) {
                signatureCompleted = false;
                finish();
            }
        }
    }

    @Override
    public void loadContent() {
        super.showProgressDialog();
        new QuoteDao(this, this.quoteId).execute();
    }

    private void displayQuote() {
        this.payeeInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.payee_title), this.quote.payeeCompanyName).getView());
        this.payeeInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.tax_no), this.quote.debtorTaxNo).getView());

        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.total_amount), Method.formatDoubleAsAmount(this.quote.totalAmount, this.quote.currency, getResources().getConfiguration().locale)).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.interest), Method.formatDoubleAsAmount(this.quote.interestAmount, this.quote.currency, getResources().getConfiguration().locale)).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.maturity_date), Method.getFormatedDate(this.quote.maturityDate, Constants.interfaceDateFormat) ).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.payment_amount_for_maturity), Method.formatDoubleAsAmount(this.quote.paymentAmount, this.quote.currency, getResources().getConfiguration().locale), true).getView());

        for (Invoice invoice: this.quote.invoices)
            this.invoicesLinearLayout.addView(new InvoiceCellViewHandler(this, invoice, R.layout.list_item_invoice_for_horizontal_2, false, null).getView());
    }

    @OnClick(R.id.approveButton)
    public void approveButtonAction(FMButton button) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(ContractActivity.QUOTE_ID_KEY, this.quote.id);
        params.put(ContractActivity.CONTRACT_TYPE_KEY, ContractActivity.CONTRACT_TYPE_DEBTOR);
        startActivity(ContractActivity.class, params);
//        super.finish();
    }

    @OnClick(R.id.declineButton)
    public void declineButtonAction(FMButton button) {
        new DateNoteInputDialog(DateNoteInputDialog.MODE_NOTE, this, this, null, null, getString(R.string.decline_request_reason_text)).show();
    }

    @OnClick(R.id.changeButton)
    public void changeButtonAction(FMButton button) {
        super.showProgressDialog();
        new DateNoteInputDialog(DateNoteInputDialog.MODE_DATE_NOTE, this, this, null, getString(R.string.pick_maturity_date), getString(R.string.change_request_reason_text)).show();
    }

    @Override
    public void dateNoteAreDetermined(Date date, String note) {
        super.showProgressDialog();
        new ChangeQuoteDao(this, new ChangeQuoteRequest(this.quoteId, note, date)).execute();
    }

    @Override
    public void noteIsDetermined(String note) {
        super.showProgressDialog();
        new DeclineQuoteDao(this, new DeclineQuoteRequest(this.quoteId, note)).execute();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.hideProgressDialog();
        if (dao instanceof QuoteDao) {
            this.quote = (Quote) data;
            this.quote.status = this.quoteStatus;
            this.quote.id = quoteId;
            this.displayQuote();
            Method.fadeIn(this.scrollView);
        } else if (dao instanceof DebtorFingerPrintDao) {
            Fingerprint fingerprint = (Fingerprint) data;
            this.fingerPrintID = fingerprint.value;
            SimpleDialog dialog = new SimpleDialog(this, SimpleDialog.Type.Info);
            dialog.setTitle(getString(R.string.mobile_signature_dialog_title));
            dialog.setContent(fingerprint.value);
            dialog.setButtonText(getString(R.string.start));
            dialog.setButtonOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    new ApproveQuoteDebtorDao(InvoiceAssessmentActivity.this, quoteId).execute();
                }
            });
            dialog.show();
        } else if (dao instanceof ApproveQuoteDebtorDao) {
            Fingerprint fingerprint = (Fingerprint) data;
            fingerprint.value = fingerPrintID;
            Intent intent = new Intent(this, MobileSignatureActivity.class);
            intent.putExtra(MobileSignatureActivity.FINGERPRINT_KEY, fingerprint);
            intent.putExtra(MobileSignatureActivity.QUOTE_ID_KEY, quoteId);
            intent.putExtra(MobileSignatureActivity.PROCESS_TYPE, MobileSignatureActivity.TYPE_DEBTOR);
            startActivity(intent);
//
//            super.finish();
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        super.hideProgressDialog();
        super.finish();
    }
}
