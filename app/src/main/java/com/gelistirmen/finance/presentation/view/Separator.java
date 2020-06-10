package com.gelistirmen.finance.presentation.view;

import android.view.View;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.activity.BaseActivity;

public class Separator {
    View view;

    public Separator(BaseActivity activity) {
        this.view = activity.getLayoutInflater().inflate(R.layout.view_separator, null);
    }

    public View getView() {
        return this.view;
    }

}
