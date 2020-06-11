package com.gelistirmen.finance.presentation.view;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.util.Method;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoiceSettingsViewHandler implements View.OnClickListener, TextWatcher {

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.serialNumberTextView) TextView serialNumberTextView;
    @BindView(R.id.invoiceNumberTextView) TextView invoiceNumberTextView;
    @BindView(R.id.sellerCheckBox) FMCheckBox sellerCheckBox;
    @BindView(R.id.buyerCheckBox) FMCheckBox buyerCheckBox;
    @BindView(R.id.allAmountCheckBox) FMCheckBox allAmountCheckBox;
    @BindView(R.id.setAmountCheckBox) FMCheckBox setAmountCheckBox;
    @BindView(R.id.amountEditText) EditText amountEditText;
    @BindView(R.id.estimatedMaturityEditText) EditText estimatedMaturityEditText;

    private BaseActivity activity;
    private LinearLayout layout;
    private LinearLayout datePickerLayout;
    private DatePicker datePicker;

    private Invoice invoice;
    private Listener listener;

    public interface Listener {
        void onDeleteInvoiceButtonClick(InvoiceSettingsViewHandler viewHandler, Invoice invoice);
    }

    public InvoiceSettingsViewHandler(BaseActivity activity, Invoice invoice) {
        this.activity = activity;
        this.invoice = invoice;

        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.view_invoice_settings, null);
        ButterKnife.bind(this, this.layout);

        if (!MyApplication.mocking)
            this.imageView.setImageBitmap(BitmapFactory.decodeByteArray(this.invoice.pictureData, 0, this.invoice.pictureData.length));
        this.serialNumberTextView.setText(this.invoice.serialNo);
        this.invoiceNumberTextView.setText(this.invoice.no);
        this.amountEditText.addTextChangedListener(this);
        this.amountEditText.setText(Method.formatDoubleAsAmount(this.invoice.amount, 0, activity.getResources().getConfiguration().locale).trim());
        this.amountEditText.setFocusable(false);
        if (this.invoice.estimatedMaturityDate != null)
            this.estimatedMaturityEditText.setText(Method.getFormatedDate(this.invoice.estimatedMaturityDate, Constants.interfaceDateFormat));

        this.sellerCheckBox.setChecked(this.invoice.interestResponsible == Constants.Role.Payee);
        this.sellerCheckBox.setOnClickListener(this);
        this.buyerCheckBox.setChecked(this.invoice.interestResponsible == Constants.Role.Debtor);
        this.buyerCheckBox.setOnClickListener(this);
        this.allAmountCheckBox.setChecked(this.invoice.isWholeCost);
        this.allAmountCheckBox.setOnClickListener(this);
        this.setAmountCheckBox.setChecked(!this.invoice.isWholeCost);
        this.setAmountCheckBox.setOnClickListener(this);

        this.datePickerLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_datepicker, null, false);
        this.datePicker = this.datePickerLayout.findViewById(R.id.datePicker);
        Date dummyDate = this.invoice.estimatedMaturityDate == null ? new Date() : this.invoice.estimatedMaturityDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dummyDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        this.datePicker.init(year, month, day, null);

    }

    public LinearLayout getView() {
        return this.layout;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == this.sellerCheckBox) {
            this.invoice.interestResponsible = Constants.Role.Payee;
            this.buyerCheckBox.setOnClickListener(null);
            this.buyerCheckBox.setChecked(false);
            this.buyerCheckBox.setOnClickListener(this);
        } else if (v == this.buyerCheckBox) {
            this.invoice.interestResponsible = Constants.Role.Debtor;
            this.sellerCheckBox.setOnClickListener(null);
            this.sellerCheckBox.setChecked(false);
            this.sellerCheckBox.setOnClickListener(this);
        } else if (v == this.allAmountCheckBox) {
            this.invoice.isWholeCost = true;
            this.setAmountCheckBox.setOnClickListener(null);
            this.setAmountCheckBox.setChecked(false);
            this.setAmountCheckBox.setOnClickListener(this);
            this.amountEditText.setText(Method.formatDoubleAsAmount(this.invoice.amount, this.invoice.currency, activity.getResources().getConfiguration().locale));
            this.amountEditText.setFocusableInTouchMode(false);
            this.amountEditText.setFocusable(false);
        } else if (v == this.setAmountCheckBox) {
            this.invoice.isWholeCost = false;
            this.allAmountCheckBox.setOnClickListener(null);
            this.allAmountCheckBox.setChecked(false);
            this.allAmountCheckBox.setOnClickListener(this);
            this.amountEditText.setSelection(this.amountEditText.getText().length());
            this.amountEditText.setFocusableInTouchMode(true);
            this.amountEditText.setFocusable(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            this.invoice.amount = Double.parseDouble(s.toString().replace(",", "."));
        } catch (Exception e) {
            this.invoice.amount = 0;
        }
    }

    @Override
    public void afterTextChanged(Editable s) { }

    @OnClick(R.id.estimatedMaturityEditText)
    public void estimatedMaturityEditTextAction(FMEditText editText) {
        new MaterialDialog.Builder(activity)
                .title(R.string.estimated_maturity_date)
                .customView(this.datePickerLayout, false)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        datePicker.setTag(false);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if ((Boolean) datePicker.getTag()) {
                            invoice.estimatedMaturityDate = Method.getDateFromDatePicker(datePicker);
                            estimatedMaturityEditText.setText(Method.getFormatedDate(invoice.estimatedMaturityDate, Constants.interfaceDateFormat));
                        }
                    }
                })
                .show();
        this.datePicker.setTag(true);
    }

    @OnClick(R.id.deleteButton)
    public void deleteButtonAction(ImageButton button) {
        if (this.listener != null)
            this.listener.onDeleteInvoiceButtonClick(this, this.invoice);
    }
}
