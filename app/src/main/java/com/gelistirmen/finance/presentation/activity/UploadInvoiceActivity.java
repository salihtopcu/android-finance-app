package com.gelistirmen.finance.presentation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MyApplication;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.CreateInvoiceDao;
import com.gelistirmen.finance.data.remote.operation.InvoiceVisionDao;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.presentation.fragment.FileChoosingOptionsFragment;
import com.gelistirmen.finance.presentation.fragment.InvoiceSettingsFragment;
import com.gelistirmen.finance.util.Method;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class UploadInvoiceActivity extends BaseActivity implements InvoiceSettingsFragment.Listener {

    private FileChoosingOptionsFragment fileChoosingOptionsFragment;
    private InvoiceSettingsFragment invoiceSettingsFragment;

    Invoice.List invoices;

    public String mCurrentPhotoPath;
    public Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_invoice);
        super.showActionBarBackButton();
        super.setActionBarTitle(R.string.upload_invoice);
        super.hideBackgroundImage();

        this.showFileChoosingFragment();

        this.invoices = new Invoice.List();
    }

    private void showFileChoosingFragment() {
        if (this.fileChoosingOptionsFragment == null)
            this.fileChoosingOptionsFragment = FileChoosingOptionsFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, this.fileChoosingOptionsFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, this.fileChoosingOptionsFragment).commit();
    }

    private void showInvoiceSettingsFragment(Invoice.List invoices) {
        if (this.invoiceSettingsFragment == null)
            this.invoiceSettingsFragment = InvoiceSettingsFragment.newInstance(this, invoices);
        else
            this.invoiceSettingsFragment.setInvoices(invoices);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, this.invoiceSettingsFragment).commit();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.hide(this.fileChoosingOptionsFragment);
//        fragmentTransaction.add(R.id.fragmentContainer, this.invoiceSettingsFragment);
//        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.FileChoosingMethod.TakePhoto || requestCode == Constants.FileChoosingMethod.ChooseFromDevice) {
                byte[] byteData = null;
                if (requestCode == Constants.FileChoosingMethod.TakePhoto) {

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                    try {
                        bmp = Method.rotateImageIfRequired(bmp,selectedImageUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteData = stream.toByteArray();
                    bmp.recycle();

                } else if (requestCode == Constants.FileChoosingMethod.ChooseFromDevice) {
                    try {
                        Uri selectedImage = data.getData();
                        InputStream iStream = getContentResolver().openInputStream(selectedImage);
                        byteData = Method.getBytes(iStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (byteData != null) {
                    super.showProgressDialog();
                    new InvoiceVisionDao(this, byteData).execute();
                }
            }
        }
    }

    @Override
    public void onAddInvoiceButtonClick() {
        this.showFileChoosingFragment();
    }

    @Override
    public void onSaveButtonClick(Invoice.List invoices) {
        for (Invoice invoice: invoices) {
            invoice.currency = Constants.Currency.TL;
//            invoice.interestResponsible = Constants.Role.Payee; // 1 satici/payee, 2 alici/debtor
            invoice.date = new Date();
            invoice.type = 1;
        }
        super.showProgressDialog();
        new CreateInvoiceDao(this, invoices).execute();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        if (dao instanceof InvoiceVisionDao) {
            super.hideProgressDialog();
            Invoice invoice = (Invoice) data;
            invoice.interestResponsible = Constants.Role.Debtor;
            invoice.isWholeCost = true;
            this.invoices.add(invoice);
            this.showInvoiceSettingsFragment(this.invoices);
        } else if (dao instanceof CreateInvoiceDao) {
            super.hideProgressDialog();
            MyApplication.messageForActivity = getString(R.string.invoice_upload_success_message) ;
            HomeActivity.requiresRefresh = true;
            finish();
        }
    }
}
