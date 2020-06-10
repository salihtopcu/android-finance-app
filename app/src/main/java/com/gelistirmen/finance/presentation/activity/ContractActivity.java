package com.gelistirmen.finance.presentation.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.DebtorContractDao;
import com.gelistirmen.finance.data.remote.operation.PayeeContractDao;
import com.gelistirmen.finance.presentation.view.FMButton;

import butterknife.BindView;
import butterknife.OnClick;

public class ContractActivity extends BaseActivity {

    public static final String QUOTE_ID_KEY = "QUOTE_ID";
    public static final String CONTRACT_TYPE_KEY = "CONTRACT_TYPE";
    public static final int CONTRACT_TYPE_PAYEE = 1;
    public static final int CONTRACT_TYPE_DEBTOR = 2;

    @BindView(R.id.pdfView) PDFView pdfView;

    String quoteId;
    int contractType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quoteId = super.getIntent().getStringExtra(QUOTE_ID_KEY);
        this.contractType = super.getIntent().getIntExtra(CONTRACT_TYPE_KEY, 0);
        setContentView(R.layout.activity_contract);
        super.setActionBarTitle(R.string.contract);
        super.hideBackgroundImage();
    }

    @Override
    public void loadContent() {
        super.showProgressDialog();
        if (this.contractType == CONTRACT_TYPE_PAYEE)
            new PayeeContractDao(this, this.quoteId).execute();
        else if (this.contractType == CONTRACT_TYPE_DEBTOR)
            new DebtorContractDao(this, this.quoteId).execute();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        PDFView.Configurator configurator = this.pdfView.fromBytes((byte[])data);
        configurator.invalidPageColor(Color.CYAN)
                .enableSwipe(true)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Log.e("pdf error", t.getMessage());
                        showErrorDialog(t.getMessage());
                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        Log.e("pdf error", t.getMessage());
                        showErrorDialog(t.getMessage());
                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        hideProgressDialog();
                    }
                });
        configurator.load();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        super.hideProgressDialog();
    }

    @OnClick(R.id.acceptButton)
    public void acceptButtonAction(FMButton button) {
        if (this.contractType == CONTRACT_TYPE_PAYEE) {
//            MyApplication.messageForActivity = getString(R.string.payee_quote_approval_success_message);
            QuoteAssessmentActivity.contractAccepted = true;
        } else if (this.contractType == CONTRACT_TYPE_DEBTOR) {
//            MyApplication.messageForActivity = getString(R.string.debtor_quote_approval_success_message);
            InvoiceAssessmentActivity.contractAccepted = true;
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
