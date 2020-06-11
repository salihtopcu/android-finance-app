package com.gelistirmen.finance.data.remote.operation;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.DocumentType;

public class UploadDocumentDao extends FMDao {

    public UploadDocumentDao(VolleyDaoListener listener, byte[] documentData, DocumentType documentType) {
        super(listener, Constants.ApiMethod.Documents, Request.Method.POST, true);
        super.setRequestType(RequestType.MultipartRequest);
        super.setMultipartData(documentData, "documentFile", "invoice_test.jpeg", "image/jpeg");
        super.addMultipartParameter("documentTypeId", documentType.id);
        super.timeout = 60000;
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest();
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return null;
    }
}