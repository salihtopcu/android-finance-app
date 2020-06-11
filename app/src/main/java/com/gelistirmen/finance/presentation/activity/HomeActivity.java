package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.ActiveFactoringProcessesDao;
import com.gelistirmen.finance.data.remote.operation.InvoicesDao;
import com.gelistirmen.finance.data.remote.operation.QuotesWaitingForApprovalDao;
import com.gelistirmen.finance.model.operation.FactoringProcess;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FactoringProcessCellViewHandler;
import com.gelistirmen.finance.presentation.view.InvoiceCellViewHandler;
import com.gelistirmen.finance.presentation.view.QuoteCellViewHandler;
import com.gelistirmen.finance.util.Method;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    public static HomeActivity instance;
    public static Boolean isVisible = false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "HomeActivity";

    public static boolean requiresRefresh = false;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.recentInvoicesLinearLayout) LinearLayout recentInvoicesLinearLayout;
    @BindView(R.id.moreInvoicesTextView) TextView moreInvoicesTextView;
    @BindView(R.id.seeAllRecentInvoicesButton) FMButton seeAllRecentInvoicesButton;
    @BindView(R.id.activeProcessesLinearLayout) LinearLayout activeProcessesLinearLayout;
    @BindView(R.id.moreProcessesTextView) TextView moreProcessesTextView;
    @BindView(R.id.seeAllProcessesButton) FMButton seeAllProcessesButton;
    @BindView(R.id.quotesLinearLayout) LinearLayout quotesLinearLayout;
    @BindView(R.id.moreQuotesTextView) TextView moreQuotesTextView;
    @BindView(R.id.seeAllQuotesButton) FMButton seeAllQuotesButton;
    @BindView(R.id.noItemInfo1TextView) TextView noItemInfo1TextView;
    @BindView(R.id.noItemInfo2TextView) TextView noItemInfo2TextView;
    @BindView(R.id.noItemInfo3TextView) TextView noItemInfo3TextView;

    private Invoice.List recentInvoices;
    private FactoringProcess.List factoringProcesses;
    private Quote.List quotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        super.showNavigationMenu();
        super.hideBackgroundImage();
        super.setMainScrollContent(this.scrollView);

        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadContent();
            }
        });

        HomeActivity.instance = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        HomeActivity.isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomeActivity.isVisible = false;
    }

    @Override
    protected void onResume() {
        if (super.isFirstLoad && !MyApplication.mocking)
            this.scrollView.setVisibility(View.INVISIBLE);
        super.onResume();
        if (!super.isFirstLoad && HomeActivity.requiresRefresh) {
            this.loadContent();
            HomeActivity.requiresRefresh = false;
        }

        HomeActivity.isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        HomeActivity.isVisible = false;
    }

    @Override
    public void loadContent() {
        if (!this.swipeRefreshLayout.isRefreshing())
            super.showProgressDialog();
        new InvoicesDao(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addInvoice)
            super.startActivity(UploadInvoiceActivity.class);
        return false;
    }

    @OnClick({R.id.seeAllRecentInvoicesButton, R.id.seeAllProcessesButton, R.id.seeAllQuotesButton})
    public void seeAllButtonAction(FMButton button) {
        HashMap<String, Object> params = new HashMap<>();
        if (button == this.seeAllRecentInvoicesButton)
            params.put(SimpleListActivity.LIST_TYPE_KEY, SimpleListActivity.ListType.recentInvoices);
        else if (button == this.seeAllProcessesButton)
            params.put(SimpleListActivity.LIST_TYPE_KEY, SimpleListActivity.ListType.activeFactoringProcesses);
        else if (button == this.seeAllQuotesButton)
            params.put(SimpleListActivity.LIST_TYPE_KEY, SimpleListActivity.ListType.quotesWaitingForApproval );
        super.startActivity(SimpleListActivity.class, params);
    }

    private void reloadRecentInvoicesArea() {
        this.recentInvoicesLinearLayout.removeAllViews();
        for (int i = 0; i < Math.min(this.recentInvoices.size(), 4); i++) {
            final Invoice invoice = this.recentInvoices.get(i);
            this.recentInvoicesLinearLayout.addView(
                    new InvoiceCellViewHandler(this, invoice, R.layout.list_item_invoice_for_horizontal, true, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, Object> params = new HashMap<>();
                            params.put(InvoiceActivity.INVOICE_ID_KEY, invoice.id);
                            startActivity(InvoiceActivity.class, params);
                        }
                    }).getView());
        }

        int moreRecentInvoicesCount = this.recentInvoices.size() - 3;
        if (moreRecentInvoicesCount > 0) {
            this.seeAllRecentInvoicesButton.setVisibility(View.VISIBLE);
            this.moreInvoicesTextView.setVisibility(View.VISIBLE);
            this.moreInvoicesTextView.setText(getString(R.string.more_invoices).replace("{number}", Integer.toString(moreRecentInvoicesCount)));
            this.noItemInfo1TextView.setVisibility(View.GONE);
        } else {
            this.seeAllRecentInvoicesButton.setVisibility(View. GONE);
            this.moreInvoicesTextView.setVisibility(View.GONE);
            this.noItemInfo1TextView.setVisibility(View.VISIBLE);
        }
        this.noItemInfo1TextView.setVisibility(this.recentInvoices.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void reloadActiveProcessesArea() {
        this.activeProcessesLinearLayout.removeAllViews();
        for (int i = 0; i < Math.min(this.factoringProcesses.size(), 4); i++) {
            final FactoringProcess factoringProcess = this.factoringProcesses.get(i);
            this.activeProcessesLinearLayout.addView(
                    new FactoringProcessCellViewHandler(this, factoringProcess, R.layout.list_item_factoring_process_for_horizontal, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (factoringProcess.status != Constants.FactoringStatus.InvoicesUploaded && factoringProcess.status != Constants.FactoringStatus.InvoicesChecked) {
                                HashMap<String, Object> params = new HashMap<>();
                                params.put(QuoteAssessmentActivity.QUOTE_ID_KEY, factoringProcess.quoteId);
                                params.put(QuoteAssessmentActivity.QUOTE_STATUS_KEY, factoringProcess.status);
                                startActivity(QuoteAssessmentActivity.class, params);
                            }
                        }
                    }).getView());
        }

        int moreRecentProcessesCount = this.factoringProcesses.size() - 3;
        if (moreRecentProcessesCount > 0) {
            this.seeAllProcessesButton.setVisibility(View.VISIBLE);
            this.moreProcessesTextView.setVisibility(View.VISIBLE);
            this.moreProcessesTextView.setText(getString(R.string.more_processes).replace("{number}", Integer.toString(moreRecentProcessesCount)));
        } else {
            this.seeAllProcessesButton.setVisibility(View. GONE);
            this.moreProcessesTextView.setVisibility(View.GONE);
        }
        this.noItemInfo2TextView.setVisibility(this.factoringProcesses.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void reloadQuotesArea() {
        this.quotesLinearLayout.removeAllViews();
        for (int i = 0; i < Math.min(this.quotes.size(), 4); i++) {
            final Quote quote = this.quotes.get(i);
            this.quotesLinearLayout.addView(
                    new QuoteCellViewHandler(this, quote,
                            R.layout.list_item_quote_for_horizontal, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, Object> params = new HashMap<>();
                            params.put(InvoiceAssessmentActivity.QUOTE_ID_KEY, quote.id);
                            params.put(InvoiceAssessmentActivity.QUOTE_STATUS_KEY, quote.status);
                            startActivity(InvoiceAssessmentActivity.class, params);
                        }
                    }).getView());
        }

        int moreQuotesCount = this.quotes.size() - 3;
        if (moreQuotesCount > 0) {
            this.seeAllQuotesButton.setVisibility(View.VISIBLE);
            this.moreQuotesTextView.setVisibility(View.VISIBLE);
            this.moreQuotesTextView.setText(getString(R.string.more_quotes).replace("{number}", Integer.toString(moreQuotesCount)));
        } else {
            this.seeAllQuotesButton.setVisibility(View.GONE);
            this.moreQuotesTextView.setVisibility(View.GONE);
        }
        this.noItemInfo3TextView.setVisibility(this.quotes.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        if (dao instanceof InvoicesDao) {
            this.recentInvoices = (Invoice.List) data;
            new ActiveFactoringProcessesDao(this).execute();
        } else if (dao instanceof ActiveFactoringProcessesDao) {
            this.factoringProcesses = (FactoringProcess.List) data;
            new QuotesWaitingForApprovalDao(this).execute();
        } else if (dao instanceof QuotesWaitingForApprovalDao) {
            this.quotes = (Quote.List) data;

            if (this.swipeRefreshLayout.isRefreshing())
                this.swipeRefreshLayout.setRefreshing(false);
            else
                super.hideProgressDialog();
            this.reloadRecentInvoicesArea();
            this.reloadActiveProcessesArea();
            this.reloadQuotesArea();
            Method.fadeIn(this.scrollView);

            if (Cache.getShowMobileSignatureInfo()) {
                super.showInfoDialog(getString(R.string.mobile_signature_info));
                Cache.setShowMobileSignatureInfo(false);
            }
        }
    }

    @Override
    public void daoDidFail(VolleyDao dao, VolleyDao.Error error) {
        if (this.swipeRefreshLayout.isRefreshing())
            this.swipeRefreshLayout.setRefreshing(false);
        super.daoDidFail(dao, error);
    }
}
