package com.gelistirmen.finance.presentation.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.view.FMButton;

public class SimpleDialog {
    private AlertDialog dialog;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView contentTextView;
    private FMButton button;

    public static class Type {
        public static final int Info = 0;
        public static final int Error = 1;
    }

    /**
     * @param activity  Activity
     * @param type      SimpleDialog.Type
     */
    public SimpleDialog(Activity activity, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_simple, null);
        builder.setView(view);
        this.dialog = builder.create();

        this.imageView = view.findViewById(R.id.imageView);
        this.titleTextView = view.findViewById(R.id.titleTextView);
        this.contentTextView = view.findViewById(R.id.contentTextView);
        this.button = view.findViewById(R.id.button);

        this.dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        if (type == Type.Info)
            this.imageView.setImageResource(R.drawable.ic_check_blue);
        else {
            this.imageView.setImageResource(R.drawable.ic_error_red);
            this.button.setBackgroundResource(R.drawable.dialog_button_background_red);
        }
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    public void setImage(int imageResource) {
        this.imageView.setImageResource(imageResource);
    }

    public void setTitle(String title) {
        this.titleTextView.setText(title);
    }

    public void setContent(String content) {
        this.contentTextView.setText(content);
    }

    public void setButtonText(String text) {
        this.button.setText(text);
    }

    public void setButtonOnClickListener(final View.OnClickListener listener) {
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                if (listener != null)
                    listener.onClick(button);
            }
        });
    }

    public void setCancelable(boolean cancelable){
        this.dialog.setCancelable(cancelable);
    }

    public void show() {
        if (!this.dialog.isShowing())
            this.dialog.show();
    }

    public void hide() {
        if (this.dialog.isShowing())
            this.dialog.dismiss();
    }
}
