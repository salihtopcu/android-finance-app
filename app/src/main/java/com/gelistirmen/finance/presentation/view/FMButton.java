package com.gelistirmen.finance.presentation.view;

import android.content.Context;
import android.util.AttributeSet;

import com.gelistirmen.finance.MyApplication;

public class FMButton extends android.support.v7.widget.AppCompatButton {

    public FMButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(MyApplication.Typefaces.blenderProBold);
    }
}
