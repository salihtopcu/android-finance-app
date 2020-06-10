package com.gelistirmen.finance.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;

public class FMEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher {
    @NonNull private String format;
    private String baseText = "";
    private boolean formatTextFlag = true;

    public FMEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setTypeface(MyApplication.Typefaces.blenderProThin);
        if (this.getHint() != null)
            super.setHint(this.getHint().toString().toUpperCase());

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FMEditText);
        this.format = array.getString(R.styleable.FMEditText_format);
        array.recycle();
    }

    public String getTextAsString() {
        if (this.format == null || this.format.isEmpty())
            return super.getText().toString();
        else
            return this.baseText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (this.format != null && this.format.length() > 0) {
            if (this.formatTextFlag) {
                this.formatTextFlag = false;
                String text = this.getText().toString();
                this.baseText = "";
                for (int i = 0; i < this.format.length(); i++) {
                    if (i < text.length()) {
                        if (text.charAt(i) != this.format.charAt(i))
                            this.baseText += text.charAt(i);
                    } else
                        break;
                }
                String formattedText = "";
                int baseTextIndex = 0;
                for (int i = 0; i < this.format.length(); i++) {
                    if (baseTextIndex < this.baseText.length()) {
                        if (this.format.charAt(i) == "#".charAt(0)) {
                            formattedText += this.baseText.charAt(baseTextIndex);
                            baseTextIndex++;
                        } else
                            formattedText += this.format.charAt(i);
                    } else
                        break;
                }
                this.setText(formattedText);
    //            this.setText(formattedText, BufferType.EDITABLE);
                this.setSelection(this.getText().length());
            } else
                this.formatTextFlag = true;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
