package com.gelistirmen.finance.presentation.view;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhart.stepview.StepView;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.FactoringProcess;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.util.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FactoringProcessCellViewHandler {

    @BindView(R.id.dateTextView) TextView dateTextView;
    @BindView(R.id.amountTextView) TextView amountTextView;
    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.stepView) StepView stepView;
    @BindView(R.id.arrowImageView) ImageView arrowImageView;

    private LinearLayout layout;

    private FactoringProcess factoringProcess;

    public FactoringProcessCellViewHandler(BaseActivity activity, @NonNull FactoringProcess factoringProcess, @LayoutRes int layoutId, @Nullable View.OnClickListener onViewClickListener) {
        this.factoringProcess = factoringProcess;
        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(layoutId, null);
        ButterKnife.bind(this, this.layout);
        if (onViewClickListener != null)
            this.layout.setOnClickListener(onViewClickListener);
        if (this.factoringProcess.status == Constants.FactoringStatus.InvoicesUploaded ||
                this.factoringProcess.status == Constants.FactoringStatus.InvoicesChecked ||
                onViewClickListener == null)
            this.arrowImageView.setVisibility(View.INVISIBLE);
        this.dateTextView.setText(Method.getFormatedDate(this.factoringProcess.createdDate, Constants.interfaceDateFormat));
        this.amountTextView.setText(Method.formatDoubleAsAmount(this.factoringProcess.amount, this.factoringProcess.currency, activity.getResources().getConfiguration().locale));
        this.descriptionTextView.setText(Constants.FactoringStatus.getDescription(this.factoringProcess.status));
        this.stepView.go(Math.min(this.factoringProcess.status, 6) - 1, true);
    }

    public LinearLayout getView() {
        return this.layout;
    }
}
