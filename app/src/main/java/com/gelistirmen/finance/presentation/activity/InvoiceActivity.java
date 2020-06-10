package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.InvoiceDao;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.presentation.view.DoublePropertyViewHandler;
import com.gelistirmen.finance.presentation.view.PropertyViewHandler;
import com.gelistirmen.finance.util.Method;

import butterknife.BindView;

public class InvoiceActivity extends BaseActivity {

    public static final String INVOICE_ID_KEY = "INVOICE_ID";

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.invoiceInfoContentLinearLayout) LinearLayout invoiceInfoContentLinearLayout;
    @BindView(R.id.paymentInfoContentLinearLayout) LinearLayout paymentInfoContentLinearLayout;

    String invoiceId;
    Invoice invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.invoiceId = super.getIntent().getStringExtra(INVOICE_ID_KEY);
        setContentView(R.layout.activity_invoice);
        super.setActionBarTitle(R.string.invoice_detail);
        super.showActionBarBackButton();
        super.hideBackgroundImage();
        super.setMainScrollContent(this.scrollView);
        this.scrollView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void loadContent() {
        super.showProgressDialog();
        new InvoiceDao(this, this.invoiceId).execute();
    }

    private void displayInvoice() {
        this.invoiceInfoContentLinearLayout.addView(new DoublePropertyViewHandler(this, getString(R.string.serial_number), this.invoice.serialNo, getString(R.string.invoice_number), this.invoice.no).getView());
        this.invoiceInfoContentLinearLayout.addView(new DoublePropertyViewHandler(this, getString(R.string.invoice_date), Method.getFormatedDate(this.invoice.date, Constants.interfaceDateFormat), getString(R.string.e_invoice_hash), this.invoice.eInvoiceHash).getView());
        this.invoiceInfoContentLinearLayout.addView(new DoublePropertyViewHandler(this, getString(R.string.type), Integer.toString(this.invoice.type), getString(R.string.estimated_maturity_date), Method.getFormatedDate(this.invoice.estimatedMaturityDate, Constants.interfaceDateFormat)).getView());
        this.invoiceInfoContentLinearLayout.addView(new DoublePropertyViewHandler(this, getString(R.string.payee_tax_no), this.invoice.payeeTaxNo, getString(R.string.payee_name), "").getView());
        this.invoiceInfoContentLinearLayout.addView(new DoublePropertyViewHandler(this, getString(R.string.debtor_tax_no), this.invoice.debtorTaxNo, getString(R.string.debtor_name), "").getView());

        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.invoice_amount), Method.formatDoubleAsAmount(this.invoice.amount, this.invoice.currency, getResources().getConfiguration().locale)).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.is_whole_cost), this.invoice.isWholeCost ? getString(R.string.yes) : getString(R.string.no)).getView());
        this.paymentInfoContentLinearLayout.addView(new PropertyViewHandler(this, getString(R.string.payment_cost), Method.formatDoubleAsAmount(this.invoice.paymentCost, this.invoice.currency, getResources().getConfiguration().locale)).getView());
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.hideProgressDialog();
        this.invoice = (Invoice) data;
        this.displayInvoice();
        Method.fadeIn(this.scrollView);
    }
}
