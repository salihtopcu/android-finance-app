package com.gelistirmen.finance.presentation.view;

import android.support.annotation.LayoutRes;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.activity.BaseActivity;

public class PropertyViewHandler {
    public String title;
    public String value;

    private LinearLayout layout;
    private TextView titleTextView;
    private TextView valueTextView;

    private PropertyViewHandler(BaseActivity activity, String title, String value, @LayoutRes int layoutId) {
        this.title = title;
        this.value = value;

        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(layoutId, null);
        this.titleTextView = this.layout.findViewById(R.id.titleTextView);
        this.valueTextView = this.layout.findViewById(R.id.valueTextView);
        this.titleTextView.setText(this.title);
        this.valueTextView.setText(this.value);
    }

    public PropertyViewHandler(BaseActivity activity, String title, String value) {
        this(activity, title, value, R.layout.view_proterty);
    }

    public PropertyViewHandler(BaseActivity activity, String title, String value, boolean isDark) {
        this(activity, title, value, isDark ? R.layout.view_proterty_dark : R.layout.view_proterty);
    }

    public LinearLayout getView() {
        return this.layout;
    }
}
