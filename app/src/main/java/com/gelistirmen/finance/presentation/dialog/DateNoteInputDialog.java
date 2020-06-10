package com.gelistirmen.finance.presentation.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.FMEditText;
import com.gelistirmen.finance.util.Method;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateNoteInputDialog {

    public static int MODE_NOTE = 0;
    public static int MODE_DATE_NOTE = 1;

    @BindView(R.id.dateDescriptionTextView) TextView dateDescriptionTextView;
    @BindView(R.id.datePicker) DatePicker datePicker;
    @BindView(R.id.noteEditText) FMEditText noteEditText;
    @BindView(R.id.okButton) FMButton okButton;

    private int mode;
    private AlertDialog dialog;
    private Listener listener;

    public interface Listener {
        void dateNoteAreDetermined(Date date, String note);
        void noteIsDetermined(String note);
    }

    /**
     * @param mode      One of {@link #MODE_DATE_NOTE}, {@link #MODE_NOTE}
     */
    public DateNoteInputDialog(int mode, @NonNull final Activity activity,
                               @NonNull final Listener listener, @Nullable Date date,
                               @Nullable String dateDescription, @Nullable String noteHint) {
        this.mode = mode;
        this.listener = listener;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_date_note_input, null);
        builder.setView(view);
        builder.setCancelable(true);
        this.dialog = builder.create();
        ButterKnife.bind(this, view);

        if (this.mode == MODE_NOTE) {
            this.datePicker.setVisibility(View.GONE);
            this.dateDescriptionTextView.setVisibility(View.GONE);
        } else if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            this.datePicker.init(year, month, day, null);
        }

        this.dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    public void show() {
        if (!this.dialog.isShowing()) {
            this.okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (mode == MODE_DATE_NOTE)
                            listener.dateNoteAreDetermined(Method.getDateFromDatePicker(datePicker), noteEditText.getTextAsString().trim());
                        else
                            listener.noteIsDetermined(noteEditText.getTextAsString().trim());
                    }
                }
            });
            this.dialog.show();
        }
    }

    public void hide() {
        if (this.dialog.isShowing())
            this.dialog.dismiss();
    }
}
