package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.QuoteStatusDao;
import com.gelistirmen.finance.model.operation.Fingerprint;
import com.gelistirmen.finance.model.operation.QuoteStatusResponse;

import butterknife.BindView;
import butterknife.OnClick;

public class MobileSignatureActivity extends BaseActivity {

    public static final String FINGERPRINT_KEY = "FINGERPRINT_KEY";
    public static final String QUOTE_ID_KEY = "QUOTE_ID";
    public static final String PROCESS_TYPE = "PROCESS_TYPE";
    public static final int TYPE_PAYEE = 1;
    public static final int TYPE_DEBTOR = 2;

    @BindView(R.id.mobileSignatureTime)
    TextView signatureTime;
    @BindView(R.id.checkStatusButton)
    ImageView checkStatusButton;
    @BindView(R.id.checkStatusProgress)
    ProgressBar checkStatusProgress;
    @BindView(R.id.fingerPrintText)
    TextView fingerPrintText;

    private Fingerprint fingerprintModel;
    private int time = 180000;
    private String id;
    private boolean isChecking = false;
    private int status = 0;
    private int processType = 0;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_signature);
//        super.showActionBarBackButton();
        super.setActionBarTitle(R.string.mobile_signature);
        super.hideBackgroundImage();

        this.fingerprintModel = (Fingerprint) getIntent().getSerializableExtra(FINGERPRINT_KEY);
        this.id = getIntent().getStringExtra(QUOTE_ID_KEY);
        this.processType = getIntent().getIntExtra(PROCESS_TYPE, 0);

        time = this.fingerprintModel.timeout <= 0 ?  180000 : fingerprintModel.timeout * 1000;
    }

    @Override
    public void loadContent() {
        this.startTimer();
        this.fingerPrintText.setText(fingerprintModel.value);
    }

    @OnClick(R.id.checkStatusButton)
    void onCheckStatusClick(){
        if (!isChecking) {
            this.checkStatus();
        }
    }

    private void startTimer() {
        this.signatureTime.setText("03:00");
        timer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                signatureTime.setText("" + millisUntilFinished / 60000 + ":" + String.format("%02d", (millisUntilFinished / 1000) % 60));

                if (((millisUntilFinished / 1000) % 10) == 0) {
                    if (!isChecking) {
                        checkStatus();
                    }
                }
            }

            public void onFinish() {

                showTimerEndDialog();
            }
        };

        timer.start();
    }

    private void showTimerEndDialog(){
        super.showErrorDialog("Süreniz Bitti. Lütfen Tekrar Deneyiniz.", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, false);
    }

    private void checkStatus(){
        this.checkStatusButton.setVisibility(View.GONE);
        this.checkStatusProgress.setVisibility(View.VISIBLE);
        this.isChecking = true;
        new QuoteStatusDao(this, this.id).execute();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        this.checkStatusButton.setVisibility(View.VISIBLE);
        this.checkStatusProgress.setVisibility(View.GONE);
        this.isChecking = false;
        super.hideProgressDialog();
        this.status = ((QuoteStatusResponse) data).status;
        if (processType == TYPE_PAYEE) {
            if (this.status == Constants.QuoteStatus.Completed) {
                if (timer != null)
                    timer.cancel();
                super.showInfoDialog(getString(R.string.mobile_signature_result_success), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuoteAssessmentActivity.signatureCompleted = true;
                        finish();
                    }
                });
            } else if (this.status == Constants.QuoteStatus.PendingPayeeSignature) {

            } else {
                if (timer != null)
                    timer.cancel();
                super.showErrorDialog(getString(R.string.mobile_signature_result_declined), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, false);
            }
        } else {

            if (this.status == Constants.QuoteStatus.PendingApproval) {
                if (timer != null)
                    timer.cancel();
                super.showInfoDialog(getString(R.string.mobile_signature_result_success), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InvoiceAssessmentActivity.signatureCompleted = true;
                        finish();
                    }
                });
            } else if (this.status == Constants.QuoteStatus.PendingDebtorSignature) {

            } else {
                if (timer != null)
                    timer.cancel();
                super.showErrorDialog(getString(R.string.mobile_signature_result_declined), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MyApplication.messageForActivity = getString(R.string.mobile_signature);
                        finish();
                    }
                }, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onStop() {

        if (timer != null)
            timer.cancel();

        super.onStop();
    }
}
