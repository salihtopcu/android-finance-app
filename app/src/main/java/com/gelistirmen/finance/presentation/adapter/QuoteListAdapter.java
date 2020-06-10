package com.gelistirmen.finance.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.presentation.activity.InvoiceAssessmentActivity;
import com.gelistirmen.finance.presentation.view.QuoteCellViewHandler;

import java.util.HashMap;

public class QuoteListAdapter extends ArrayAdapter<Quote> {

    public QuoteListAdapter(BaseActivity activity, @NonNull Quote.List quotes) {
        super(activity, 0, quotes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Quote quote = super.getItem(position);
        return new QuoteCellViewHandler((BaseActivity) getContext(), quote, R.layout.list_item_quote, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> params = new HashMap<>();
                params.put(InvoiceAssessmentActivity.QUOTE_ID_KEY, quote.id);
                ((BaseActivity) getContext()).startActivity(InvoiceAssessmentActivity.class, params);
            }
        }).getView();
    }
}
