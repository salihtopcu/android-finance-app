package com.gelistirmen.finance.presentation.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.presentation.activity.UploadInvoiceActivity;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.util.Method;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FileChoosingOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileChoosingOptionsFragment extends Fragment {

    String mCurrentPhotoPath;

    public FileChoosingOptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FileChoosingOptionsFragment.
     */
    public static FileChoosingOptionsFragment newInstance() {
        FileChoosingOptionsFragment fragment = new FileChoosingOptionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_choosing_options, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.takePhotoButton)
    public void takePhotoButtonAction(FMButton button) {
        dispatchTakePictureIntent();

    }

    @OnClick(R.id.chooseFromDeviceButton)
    public void chooseFromDeviceButtonAction(FMButton button) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        super.getActivity().startActivityForResult(pickPhoto , Constants.FileChoosingMethod.ChooseFromDevice);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Method.createImageFile(getActivity());
                mCurrentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.gelistirmen.finance.fileprovider",
                        photoFile);

                // TODO: Buralar biraz sağlıksız oldu abi bir kontrol edersen iyi olur.
                ((UploadInvoiceActivity) getActivity()).selectedImageUri = photoURI;
                ((UploadInvoiceActivity) getActivity()).mCurrentPhotoPath = mCurrentPhotoPath;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                super.getActivity().startActivityForResult(takePictureIntent, Constants.FileChoosingMethod.TakePhoto);
            }
        }
    }

}
