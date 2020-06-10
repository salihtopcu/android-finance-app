package com.gelistirmen.finance.presentation.view;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.activity.BaseActivity;

public class DoublePropertyViewHandler {

    private LinearLayout layout;

    public DoublePropertyViewHandler(BaseActivity activity, String title1, String value1, String title2, String value2) {
        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.view_double_proterty, null);
        ((TextView) this.layout.findViewById(R.id.title1TextView)).setText(title1);
        ((TextView) this.layout.findViewById(R.id.value1TextView)).setText(value1);
        ((TextView) this.layout.findViewById(R.id.title2TextView)).setText(title2);
        ((TextView) this.layout.findViewById(R.id.value2TextView)).setText(value2);
    }

    public LinearLayout getView() {
        return this.layout;
    }
}
