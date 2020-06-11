package com.gelistirmen.finance.data.remote.operation;

import android.support.annotation.Nullable;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MockProvider;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.DocumentType;

import org.json.JSONArray;

public class DocumentTypesDao extends FMDao {

    public DocumentTypesDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.DocumentTypes);
        super.setRequestType(RequestType.JsonArrayRequest);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        DocumentType.List documentTypes = null;
        if (data instanceof JSONArray)
            documentTypes = new DocumentType.List((JSONArray) data);
        super.onAfterSuccessRequest(documentTypes);
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return MockProvider.documentTypes();
    }
}
