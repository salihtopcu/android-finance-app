package com.gelistirmen.finance.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.presentation.activity.InvoiceActivity;
import com.gelistirmen.finance.presentation.view.InvoiceCellViewHandler;

import java.util.HashMap;

public class InvoiceListAdapter extends ArrayAdapter<Invoice> {

    public InvoiceListAdapter(BaseActivity activity, @NonNull Invoice.List invoiceList) {
        super(activity, 0, invoiceList);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return new InvoiceCellViewHandler((BaseActivity) getContext(), super.getItem(position), R.layout.list_item_invoice, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> params = new HashMap<>();
                params.put(InvoiceActivity.INVOICE_ID_KEY, getItem(position).id);
                ((BaseActivity)getContext()).startActivity(InvoiceActivity.class, params);
            }
        }).getView();
    }
}
