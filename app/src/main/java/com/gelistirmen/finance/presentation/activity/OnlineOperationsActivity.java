package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.view.ContentButtonViewHandler;

import butterknife.BindView;

public class OnlineOperationsActivity extends BaseActivity {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.linearLayout) LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_online_operations);
        super.hideBackgroundImage();
        super.showActionBarBackButton();
        super.setMainScrollContent(this.scrollView);

        super.setActionBarTitle(R.string.online_operations);

        linearLayout.addView(new ContentButtonViewHandler(this, null, getString(R.string.invoice_report), getString(R.string.sample_medium_text), R.drawable.ic_document_black, null).getView());
        linearLayout.addView(new ContentButtonViewHandler(this, null, getString(R.string.risk_report), getString(R.string.sample_medium_text), R.drawable.ic_risc_report_black , null).getView());
        linearLayout.addView(new ContentButtonViewHandler(this, null, getString(R.string.check_bond_report), getString(R.string.sample_medium_text), R.drawable.ic_check_report_black, null).getView());
        linearLayout.addView(new ContentButtonViewHandler(this, null, getString(R.string.extract), getString(R.string.sample_medium_text), R.drawable.ic_extract_black, null).getView());
    }
}
