package com.gelistirmen.finance.presentation.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.view.FMButton;

public class ConfirmDialog {
    private AlertDialog dialog;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView contentTextView;
    private FMButton cancelButton;
    private FMButton confirmButton;

    public ConfirmDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        builder.setView(view);
        this.dialog = builder.create();

        this.imageView = view.findViewById(R.id.imageView);
        this.titleTextView = view.findViewById(R.id.titleTextView);
        this.contentTextView = view.findViewById(R.id.contentTextView);
        this.cancelButton = view.findViewById(R.id.cancelButton);
        this.confirmButton = view.findViewById(R.id.confirmButton);

        this.dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        this.confirmButton.setOnClickListener(new View.OnClickListener() {
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

    public void setCancelButtonText(String text) {
        this.cancelButton.setText(text);
    }

    public void setConfirmButtonText(String text) {
        this.confirmButton.setText(text);
    }

    public void setCancelButtonOnClickListener(final View.OnClickListener listener) {
        if (listener != null) {
            this.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                    listener.onClick(cancelButton);
                }
            });
        }
    }

    public void setConfirmButtonOnClickListener(final View.OnClickListener listener) {
        this.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                if (listener != null)
                    listener.onClick(confirmButton);
            }
        });
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
