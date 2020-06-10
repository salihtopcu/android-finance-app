package com.gelistirmen.finance.presentation.view;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.membership.BankAccount;
import com.gelistirmen.finance.presentation.activity.BaseActivity;

public class BankAccountViewHandler {
    private BankAccount bankAccount;

    private LinearLayout layout;
    private FMRadioButton radioButton;
    private TextView branchNameTextView;
    private TextView bankNameTextView;
    private TextView noTextView;
    private ImageButton editButton;
    private ImageButton deleteButton;

    private Listener listener;

    public interface Listener {
        void onCheckedChanged(BankAccountViewHandler bankAccountViewHandler, BankAccount bankAccount, boolean isChecked);
    }

    private BankAccountViewHandler(BaseActivity activity, final BankAccount bankAccount, @Nullable View.OnClickListener onViewClickListener, @Nullable View.OnClickListener onEditButtonClickListener, @Nullable View.OnClickListener onDeleteButtonClickListener, boolean hasRadioButton, @Nullable final Listener listener) {
        this.bankAccount = bankAccount;
        this.listener = listener;
        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.list_item_bank_account, null);
        if (onViewClickListener != null)
            this.layout.setOnClickListener(onViewClickListener);
        else if (hasRadioButton)
            this.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radioButton.setChecked(true);
                }
            });

        this.radioButton = this.layout.findViewById(R.id.radioButton);
        this.branchNameTextView = this.layout.findViewById(R.id.branchNameTextView);
        this.bankNameTextView = this.layout.findViewById(R.id.bankNameTextView);
        this.noTextView = this.layout.findViewById(R.id.noTextView);
        this.editButton = this.layout.findViewById(R.id.editButton);
        this.deleteButton = this.layout.findViewById(R.id.deleteButton);

        this.radioButton.setVisibility(hasRadioButton ? View.VISIBLE : View.GONE);
        this.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null)
                    listener.onCheckedChanged(BankAccountViewHandler.this, bankAccount, isChecked);
            }
        });
        this.branchNameTextView.setText(this.bankAccount.branchCode);
        this.bankNameTextView.setText(this.bankAccount.bankName);
        this.noTextView.setText(this.bankAccount.iban);

        if (onEditButtonClickListener != null)
            this.editButton.setOnClickListener(onEditButtonClickListener);
        else
            this.editButton.setVisibility(View.GONE);
        if (onDeleteButtonClickListener != null)
            this.deleteButton.setOnClickListener(onDeleteButtonClickListener);
        else
            this.deleteButton.setVisibility(View.GONE);
    }

    public BankAccountViewHandler(BaseActivity activity, BankAccount bankAccount, View.OnClickListener onViewClickListener, View.OnClickListener onEditButtonClickListener, View.OnClickListener onDeleteButtonClickListener) {
        this(activity, bankAccount, onViewClickListener, onEditButtonClickListener, onDeleteButtonClickListener, false, null);
    }

    public BankAccountViewHandler(BaseActivity activity, BankAccount bankAccount, boolean hasRadioButton, Listener listener) {
        this(activity, bankAccount, null, null, null, true, listener);
    }

    public FMRadioButton getRadioButton() {
        return radioButton;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public LinearLayout getView() {
        return this.layout;
    }
}
