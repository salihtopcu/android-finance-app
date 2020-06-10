package com.gelistirmen.finance.presentation.dialog;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.membership.RegisterConfirmDao;
import com.gelistirmen.finance.data.remote.membership.ResendVerificationDao;
import com.gelistirmen.finance.presentation.view.FMButton;

import java.util.ArrayList;

public class SecurityCodeDialog implements VolleyDao.VolleyDaoListener, View.OnClickListener {
    private AlertDialog dialog;
    private ArrayList<ImageView> circles;
    private ArrayList<TextView> numbers;
    public EditText codeEditText;
    private FMButton resendButton;
    private FMButton continueButton;
    private LinearLayout progressBarLinearLayout;
    private Activity activity;
    private Listener listener;
    private String phoneNumber;

    public interface Listener {
        void verificationDidSuccess();
    }

    public SecurityCodeDialog(@NonNull final Activity activity, @NonNull final Listener listener, String phoneNumber) {
        this.activity = activity;
        this.listener = listener;
        this.phoneNumber = phoneNumber;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_security_code, null);
        builder.setView(view);
        this.dialog = builder.create();

        this.circles = new ArrayList<>();
        this.circles.add((ImageView) view.findViewById(R.id.circle1));
        this.circles.add((ImageView) view.findViewById(R.id.circle2));
        this.circles.add((ImageView) view.findViewById(R.id.circle3));
        this.circles.add((ImageView) view.findViewById(R.id.circle4));
        this.circles.add((ImageView) view.findViewById(R.id.circle5));
        this.circles.add((ImageView) view.findViewById(R.id.circle6));

        this.numbers = new ArrayList<>();
        this.numbers.add((TextView) view.findViewById(R.id.number1));
        this.numbers.add((TextView) view.findViewById(R.id.number2));
        this.numbers.add((TextView) view.findViewById(R.id.number3));
        this.numbers.add((TextView) view.findViewById(R.id.number4));
        this.numbers.add((TextView) view.findViewById(R.id.number5));
        this.numbers.add((TextView) view.findViewById(R.id.number6));

        this.codeEditText = view.findViewById(R.id.codeEditText);
        this.codeEditText.setTag(true);

        this.codeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < 6; i++) {
                    String text = s.toString();
                    if (text.length() > i) {
                        circles.get(i).setVisibility(View.INVISIBLE);
                        numbers.get(i).setVisibility(View.VISIBLE);
                        numbers.get(i).setText(text.substring(i, i + 1));
                    } else {
                        circles.get(i).setVisibility(View.VISIBLE);
                        numbers.get(i).setVisibility(View.INVISIBLE);
                        numbers.get(i).setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        this.resendButton = view.findViewById(R.id.resendButton);
        this.continueButton = view.findViewById(R.id.continueButton);
        this.progressBarLinearLayout = view.findViewById(R.id.progressBarLinearLayout);

        this.dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    private void showProgress() {
        this.progressBarLinearLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        this.progressBarLinearLayout.setVisibility(View.GONE);
    }

    public void show(boolean showTimer) {
        if (!this.dialog.isShowing()) {
            this.codeEditText.setText("");
            this.hideProgress();
            this.dialog.show();
            if (showTimer) {
                this.enableContinueButton();
                this.disableResendButton();
                this.startTimer();
            } else {
                this.disableContinueButton();
                this.enableResendButton();
            }
        }
    }

    public void hide() {
        if (this.dialog.isShowing())
            this.dialog.dismiss();
    }

    private void enableContinueButton() {
        this.continueButton.setOnClickListener(this);
        continueButton.setAlpha(1f);
    }

    private void disableContinueButton() {
        continueButton.setOnClickListener(null);
        continueButton.setAlpha(0.5f);
    }

    private void enableResendButton() {
        resendButton.setText(activity.getString(R.string.resend));
        resendButton.setOnClickListener(this);
    }

    private void disableResendButton() {
        resendButton.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        if (v == this.continueButton) {
            String codeString = codeEditText.getText().toString();
            if (codeString.length() == 6) {
                showProgress();
                new RegisterConfirmDao( SecurityCodeDialog.this, phoneNumber, codeString).execute();
            }
        } else if (v == this.resendButton) {
            showProgress();
            new ResendVerificationDao(SecurityCodeDialog.this, phoneNumber).execute();
            codeEditText.setText("");
        }
    }

    private void startTimer() {
        this.resendButton.setText("3:00");
        new CountDownTimer(Constants.securityCodeDialogTimerInterval, 1000) {
            public void onTick(long millisUntilFinished) {
                resendButton.setText("" + millisUntilFinished / 60000 + ":" + String.format("%02d", (millisUntilFinished / 1000) % 60));
            }

            public void onFinish() {
                enableResendButton();
                disableContinueButton();
            }
        }.start();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        hideProgress();
        if (dao instanceof RegisterConfirmDao) {
            this.hide();
            this.listener.verificationDidSuccess();
        } else if (dao instanceof ResendVerificationDao) {
            Toast.makeText(activity, "Verification message sent to your phone.", Toast.LENGTH_LONG).show();
            this.enableContinueButton();
            this.disableResendButton();
            this.startTimer();
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) { }

    @Override
    public void daoDidFail(VolleyDao dao, VolleyDao.Error error) {
        this.codeEditText.setText("");
        hideProgress();
        Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show();
    }
}
