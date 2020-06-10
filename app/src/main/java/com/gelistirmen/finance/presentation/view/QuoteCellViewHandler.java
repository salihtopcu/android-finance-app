package com.gelistirmen.finance.presentation.view;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.util.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuoteCellViewHandler {

    @BindView(R.id.debtorTextView) TextView debtorTextView;
    @BindView(R.id.processDateTextView) TextView processDateTextView;
    @BindView(R.id.maturityDateTextView) TextView maturityDateTextView;
    @BindView(R.id.invoiceAmountTextView) TextView invoiceAmountTextView;
    @BindView(R.id.arrowImageView) ImageView arrowImageView;

    private LinearLayout layout;

    private Quote quote;

    public QuoteCellViewHandler(BaseActivity activity, @NonNull Quote quote, @LayoutRes int layoutId, @Nullable View.OnClickListener onViewClickListener) {
        this.quote = quote;
        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(layoutId, null);
        ButterKnife.bind(this, this.layout);
        if (onViewClickListener != null)
            this.layout.setOnClickListener(onViewClickListener);
        else
            this.arrowImageView.setVisibility(View.INVISIBLE);
        this.debtorTextView.setText(this.quote.payeeCompanyName);
        this.processDateTextView.setText(Method.getFormatedDate(this.quote.updatedDate, Constants.interfaceDateFormat));
        this.maturityDateTextView.setText(Method.getFormatedDate(this.quote.maturityDate, Constants.interfaceDateFormat));
        this.invoiceAmountTextView.setText(Method.formatDoubleAsAmount(this.quote.invoiceAmount, this.quote.currency, activity.getResources().getConfiguration().locale));
    }

    public LinearLayout getView() {
        return this.layout;
    }
}
