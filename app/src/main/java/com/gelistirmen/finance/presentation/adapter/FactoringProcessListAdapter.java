package com.gelistirmen.finance.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.FactoringProcess;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.presentation.activity.QuoteAssessmentActivity;
import com.gelistirmen.finance.presentation.view.FactoringProcessCellViewHandler;

import java.util.HashMap;

public class FactoringProcessListAdapter extends ArrayAdapter<FactoringProcess> {

    public FactoringProcessListAdapter(BaseActivity activity, @NonNull FactoringProcess.List factoringProcesses) {
        super(activity, 0, factoringProcesses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FactoringProcess factoringProcess = super.getItem(position);
        return new FactoringProcessCellViewHandler((BaseActivity) getContext(), factoringProcess, R.layout.list_item_factoring_process, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (factoringProcess.status != Constants.FactoringStatus.InvoicesUploaded && factoringProcess.status != Constants.FactoringStatus.InvoicesChecked) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put(QuoteAssessmentActivity.QUOTE_ID_KEY, factoringProcess.quoteId);
                    params.put(QuoteAssessmentActivity.QUOTE_STATUS_KEY, factoringProcess.status);
                    ((BaseActivity) getContext()).startActivity(QuoteAssessmentActivity.class, params);
                }
            }
        }).getView();
    }
}
