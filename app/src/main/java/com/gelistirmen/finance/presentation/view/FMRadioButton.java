package com.gelistirmen.finance.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.gelistirmen.finance.R;

public class FMRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    private int activeColor;
    private int passiveColor;

    private OnCheckedChangeListener listener;

    public FMRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FMRadioButton);
        this.activeColor = array.getInt(R.styleable.FMCheckBox_activeColor, 0);
        this.passiveColor = array.getInt(R.styleable.FMCheckBox_passiveColor, 0);
        array.recycle();
        this.setChecked(super.isChecked());
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
        this.listener = listener;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (checked && this.activeColor != 0)
            super.setTextColor(getContext().getResources().getColor(R.color.accent));
        else if (!checked && this.passiveColor != 0)
            super.setTextColor(getContext().getResources().getColor(R.color.accent_weak));
    }

    public void setCheckedWithoutListener(boolean checked) {
        super.setOnCheckedChangeListener(null);
        this.setChecked(checked);
        super.setOnCheckedChangeListener(this.listener);
    }
}
