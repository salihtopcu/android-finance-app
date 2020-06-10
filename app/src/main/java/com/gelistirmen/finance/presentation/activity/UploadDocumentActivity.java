package com.gelistirmen.finance.presentation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.DocumentTypesDao;
import com.gelistirmen.finance.data.remote.operation.UploadDocumentDao;
import com.gelistirmen.finance.model.membership.DocumentType;
import com.gelistirmen.finance.presentation.view.DocumentTypeViewHandler;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.util.Method;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class UploadDocumentActivity extends BaseActivity implements DocumentTypeViewHandler.Listener {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.documentTypesLinearLayout) LinearLayout documentTypesLinearLayout;

    private DocumentType.List documentTypes;
    private DocumentType currentDocumentType;

    private String mCurrentPhotoPath;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        super.setActionBarTitle(R.string.upload_document);
        super.showActionBarBackButton();
        super.hideBackgroundImage();
        super.setMainScrollContent(this.scrollView);

        this.downloadDocumentTypes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.scrollView.setVisibility(View.INVISIBLE);
    }

    private void downloadDocumentTypes() {
        super.showProgressDialog();
        new DocumentTypesDao(this).execute();
    }

    private void refreshDocumentTypeList() {
        this.documentTypesLinearLayout.removeAllViews();
        for (DocumentType documentType: this.documentTypes) {
            this.documentTypesLinearLayout.addView(new DocumentTypeViewHandler(this, documentType, this).getView());
        }
    }

    @OnClick(R.id.uploadFromWebButton)
    public void uploadFromWebButtonAction(FMButton button) {
        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
        httpIntent.setData(Uri.parse(Constants.webSiteUrl));
        startActivity(httpIntent);
    }

    @Override
    public void uploadButtonAction(DocumentType documentType) {
        this.currentDocumentType = documentType;
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        super.startActivityForResult(pickPhoto , Constants.FileChoosingMethod.ChooseFromDevice);
    }

    @Override
    public void takePhotoButtonAction(DocumentType documentType) {
        this.currentDocumentType = documentType;
        dispatchTakePictureIntent();
//        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        super.startActivityForResult(takePicture, Constants.FileChoosingMethod.TakePhoto);
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

//                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byteData = stream.toByteArray();
//                    bmp.recycle();
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
                    new UploadDocumentDao(this, byteData, this.currentDocumentType).execute();
                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Method.createImageFile(this);
                mCurrentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.gelistirmen.finance.fileprovider",
                        photoFile);
                this.selectedImageUri = photoURI;

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Constants.FileChoosingMethod.TakePhoto);
            }
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        if (dao instanceof DocumentTypesDao) {
            super.hideProgressDialog();
            this.documentTypes = (DocumentType.List) data;
            this.refreshDocumentTypeList();
            Method.fadeIn(this.scrollView);
        }
    }

    @Override
    public void daoDidSuccess(VolleyDao dao) {
        if (dao instanceof UploadDocumentDao)
            this.downloadDocumentTypes();
    }
}
