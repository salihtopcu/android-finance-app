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
import com.gelistirmen.finance.data.remote.operation.ApproveQuoteByPayeeDao;
import com.gelistirmen.finance.data.remote.operation.ChangeQuoteDao;
import com.gelistirmen.finance.data.remote.operation.CompleteQuoteDao;
import com.gelistirmen.finance.data.remote.operation.DeclineQuoteDao;
import com.gelistirmen.finance.data.remote.operation.PayeeFingerPrintDao;
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

import butterknife.BindView;
import butterknife.OnClick;

public class QuoteAssessmentActivity extends BaseActivity implements DateNoteInputDialog.Listener {

    public static final String QUOTE_ID_KEY = "QUOTE_ID";
    public static final String QUOTE_STATUS_KEY = "QUOTE_STATUS";

    public static boolean contractAccepted = false;
    public static boolean signatureCompleted = false;

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.customerInfoContentLinearLayout) LinearLayout customerInfoContentLinearLayout;
    @BindView(R.id.paymentInfoContentLinearLayout) LinearLayout paymentInfoContentLinearLayout;
    @BindView(R.id.invoicesLinearLayout) LinearLayout invoicesLinearLayout;
    @BindView(R.id.changeButton) FMButton changeButton;
    @BindView(R.id.approveButton) FMButton approveButton;
    @BindView(R.id.declineButton) FMButton declineButton;
    @BindView(R.id.completeButton) FMButton completeButton;

    private String quoteId;
    private int quoteStatus;
    public static Quote quote;
    private String fingerPrintID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quoteId = super.getIntent().getStringExtra(QUOTE_ID_KEY);
        this.quoteStatus = super.getIntent().getIntExtra(QUOTE_STATUS_KEY, 0);
        super.setContentView(R.layout.activity_quote_assessment);
        super.showActionBarBackButton();
        super.setActionBarTitle(R.string.quote);
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
                new PayeeFingerPrintDao(this, this.quoteId).execute();
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
        this.customerInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.company_name), this.quote.payeeCompanyName).getView());
        this.customerInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.customer_representative), this.quote.debtorFullName).getView());
        this.customerInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.tax_no), this.quote.debtorTaxNo).getView());

        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.total_amount), Method.formatDoubleAsAmount(this.quote.totalAmount, this.quote.currency, getResources().getConfiguration().locale)).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.interest), Method.formatDoubleAsAmount(this.quote.interestAmount, this.quote.currency, getResources().getConfiguration().locale)).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.interest_rate), Integer.toString(this.quote.interestRate) + " %" ).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.maturity_date), Method.getFormatedDate(this.quote.maturityDate, Constants.interfaceDateFormat) ).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.payment_amount), Method.formatDoubleAsAmount(this.quote.paymentAmount, this.quote.currency, getResources().getConfiguration().locale), true).getView());

        for (Invoice invoice: this.quote.invoices)
            this.invoicesLinearLayout.addView(new InvoiceCellViewHandler(this, invoice, R.layout.list_item_invoice_for_horizontal_2, false, null).getView());

        this.changeButton.setVisibility(this.quote.status == Constants.FactoringStatus.QuotePrepared ? View.VISIBLE : View.GONE);
        this.approveButton.setVisibility(this.quote.status == Constants.FactoringStatus.QuotePrepared ? View.VISIBLE : View.GONE);
        this.declineButton.setVisibility(this.quote.status == Constants.FactoringStatus.QuotePrepared ? View.VISIBLE : View.GONE);
        this.completeButton.setVisibility(this.quote.status == Constants.FactoringStatus.DebtorApproved ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.changeButton, R.id.approveButton, R.id.declineButton, R.id.completeButton})
    public void buttonAction(FMButton button) {
        if (button == this.changeButton)
            new DateNoteInputDialog(DateNoteInputDialog.MODE_DATE_NOTE, this, this, null, getString(R.string.pick_maturity_date), getString(R.string.change_request_reason_text)).show();
        else if (button == this.approveButton){
            super.showProgressDialog();
            new ApproveQuoteByPayeeDao(this, this.quoteId).execute();
        }
        else if (button == this.declineButton)
            new DateNoteInputDialog(DateNoteInputDialog.MODE_NOTE, this, this, null, null, getString(R.string.decline_request_reason_text)).show();
        else if (button == this.completeButton)
            super.startActivity(EftOrderActivity.class);
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
            this.quote.id = this.quoteId;
            this.displayQuote();
            Method.fadeIn(this.scrollView);
        } else if (dao instanceof PayeeFingerPrintDao) {
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
                    new CompleteQuoteDao(QuoteAssessmentActivity.this, quoteId).execute();
                }
            });
            dialog.show();
        } else if (dao instanceof CompleteQuoteDao) {
            Fingerprint fingerprint = (Fingerprint) data;
            fingerprint.value = fingerPrintID;
            Intent intent = new Intent(this, MobileSignatureActivity.class);
            intent.putExtra(MobileSignatureActivity.FINGERPRINT_KEY, fingerprint);
            intent.putExtra(MobileSignatureActivity.QUOTE_ID_KEY, quoteId);
            intent.putExtra(MobileSignatureActivity.PROCESS_TYPE, MobileSignatureActivity.TYPE_PAYEE);
            startActivity(intent);
        }
    }

//    @Override
//    public void daoDidSuccess(VolleyDao dao) {
//        super.hideProgressDialog();
//        if (dao instanceof CompleteQuoteDao) {
//            HashMap<String, Object> params = new HashMap<>();
//            params.put(ContractActivity.QUOTE_ID_KEY, this.quote.id);
//            params.put(ContractActivity.CONTRACT_TYPE_KEY, ContractActivity.CONTRACT_TYPE_PAYEE);
//            startActivity(ContractActivity.class, params);
//        }
//        super.finish();
//    }
}
