package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.ActiveFactoringProcessesDao;
import com.gelistirmen.finance.data.remote.operation.InvoicesDao;
import com.gelistirmen.finance.data.remote.operation.QuotesWaitingForApprovalDao;
import com.gelistirmen.finance.model.operation.FactoringProcess;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.presentation.adapter.FactoringProcessListAdapter;
import com.gelistirmen.finance.presentation.adapter.InvoiceListAdapter;
import com.gelistirmen.finance.presentation.adapter.QuoteListAdapter;

import butterknife.BindView;

public class SimpleListActivity extends BaseActivity {

    public static final String LIST_TYPE_KEY = "LIST_TYPE";

    public static final class ListType {
        public static final int recentInvoices = 0;
        public static final int activeFactoringProcesses = 1;
        public static final int quotesWaitingForApproval = 2;
    }

    @BindView(R.id.listView) ListView listView;

    int listType;
    Invoice.List invoices;
    FactoringProcess.List factoringProcesses;
    Quote.List quotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listType = super.getIntent().getIntExtra(LIST_TYPE_KEY, 0);
        super.setContentView(R.layout.activity_simple_list);
        super.hideBackgroundImage();
        super.showActionBarBackButton();
        super.setMainScrollContent(this.listView);
        switch (this.listType) {
            case ListType.recentInvoices: super.setActionBarTitle(getString(R.string.invoices)); break;
            case ListType.activeFactoringProcesses: super.setActionBarTitle(getString(R.string.my_active_factoring_processes)); break;
            case ListType.quotesWaitingForApproval: super.setActionBarTitle(getString(R.string.invoices_waiting_for_approval)); break;
        }
    }

    @Override
    public void loadContent() {
        super.showProgressDialog();
        switch (this.listType) {
            case ListType.recentInvoices: new InvoicesDao(this).execute(); break;
            case ListType.activeFactoringProcesses: new ActiveFactoringProcessesDao(this).execute(); break;
            case ListType.quotesWaitingForApproval: new QuotesWaitingForApprovalDao(this).execute(); break;
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        super.hideProgressDialog();
        if (dao instanceof InvoicesDao) {
            this.invoices = (Invoice.List) data;
            this.listView.setAdapter(new InvoiceListAdapter(this, this.invoices));
        } else if (dao instanceof ActiveFactoringProcessesDao) {
            this.factoringProcesses = (FactoringProcess.List) data;
            this.listView.setAdapter(new FactoringProcessListAdapter(this, this.factoringProcesses));
        } else if (dao instanceof QuotesWaitingForApprovalDao) {
            this.quotes = (Quote.List) data;
            this.listView.setAdapter(new QuoteListAdapter(this, this.quotes));
        }
    }
}
