package com.gelistirmen.finance.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.gelistirmen.finance.R;

public class FMCheckBox extends android.support.v7.widget.AppCompatCheckBox {
    private int activeColor;
    private int passiveColor;
    private boolean uncheckable;

    public FMCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FMCheckBox);
        this.activeColor = array.getInt(R.styleable.FMCheckBox_activeColor, 0);
        this.passiveColor = array.getInt(R.styleable.FMCheckBox_passiveColor, 0);
        this.uncheckable = array.getBoolean(R.styleable.FMCheckBox_uncheckable, true);
        array.recycle();
        this.setChecked(super.isChecked());
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        // yeni eklenen property'ler üzerinden gelen renklere buradan ulaşılamadı
//        if (checked && this.activeColor != 0)
//            super.setTextColor(getContext().getResources().getColor(this.activeColor));
//        else if (!checked && this.passiveColor != 0)
//            super.setTextColor(getContext().getResources().getColor(this.passiveColor));
        if (checked && this.activeColor != 0)
            super.setTextColor(getContext().getResources().getColor(R.color.accent));
        else if (!checked && this.passiveColor != 0)
            super.setTextColor(getContext().getResources().getColor(R.color.accent_weak));
        if (!this.uncheckable)
            super.setClickable(!checked);
    }
}
