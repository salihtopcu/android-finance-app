package com.gelistirmen.finance.presentation.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.membership.DocumentType;
import com.gelistirmen.finance.presentation.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentTypeViewHandler {

    @BindView(R.id.titleTextView) TextView titleTextView;
    @BindView(R.id.uploadButton) FMButton uploadButton;
    @BindView(R.id.takePhotoButton) FMButton takePhotoButton;

    private LinearLayout layout;

    private DocumentType documentType;
    private Listener listener;

    public interface Listener {
        void uploadButtonAction(DocumentType documentType);
        void takePhotoButtonAction(DocumentType documentType);
    }

    public DocumentTypeViewHandler(BaseActivity activity, @NonNull final DocumentType documentType, final Listener listener) {
        this.documentType = documentType;
        this.listener = listener;
        this.layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.view_document_type, null);
        ButterKnife.bind(this, this.layout);
        this.titleTextView.setText(documentType.name);
        this.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.uploadButtonAction(documentType);
            }
        });
        this.takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.takePhotoButtonAction(documentType);
            }
        });
    }

    public LinearLayout getView() {
        return this.layout;
    }
}
