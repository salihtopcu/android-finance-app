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
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.util.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceCellViewHandler {

    @BindView(R.id.numberTextView) TextView numberTextView;
    @BindView(R.id.processDateTextView) TextView processDateTextView;
    @BindView(R.id.invoiceAmountTextView) TextView invoiceAmountTextView;
    @BindView(R.id.amountTextView) TextView amountTextView;

    ImageView arrowImageView;

    private LinearLayout layout;

    private Invoice invoice;

    public InvoiceCellViewHandler(BaseActivity activity, @NonNull Invoice invoice, @LayoutRes int layoutId, boolean hasArrow, @Nullable View.OnClickListener onViewClickListener) {
        this.invoice = invoice;
        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(layoutId, null);
        ButterKnife.bind(this, this.layout);
        if (onViewClickListener != null)
            this.layout.setOnClickListener(onViewClickListener);
        if (this.numberTextView != null)
            this.numberTextView.setText(invoice.serialNo + " " + invoice.no);
        if (this.processDateTextView != null)
            this.processDateTextView.setText(Method.getFormatedDate(this.invoice.updatedDate, Constants.interfaceDateFormat));
        if (this.invoiceAmountTextView != null)
            this.invoiceAmountTextView.setText(Method.formatDoubleAsAmount(this.invoice.paymentCost, this.invoice.currency, activity.getResources().getConfiguration().locale));
        if (this.amountTextView != null)
            this.amountTextView.setText(Method.formatDoubleAsAmount(this.invoice.amount, this.invoice.currency, activity.getResources().getConfiguration().locale));
        if (this.layout.findViewById(R.id.arrowImageView) != null) {
            this.arrowImageView = this.layout.findViewById(R.id.arrowImageView);
            this.arrowImageView.setVisibility(hasArrow ? View.VISIBLE : View.GONE);
        }
    }

    public LinearLayout getView() {
        return this.layout;
    }
}
