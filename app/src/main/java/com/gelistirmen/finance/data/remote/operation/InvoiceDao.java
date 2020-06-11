package com.gelistirmen.finance.data.remote.operation;

import android.support.annotation.Nullable;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MockProvider;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Invoice;

import org.json.JSONObject;

public class InvoiceDao extends FMDao {

    public InvoiceDao(VolleyDaoListener listener, String id) {
        super(listener, Constants.ApiMethod.Invoices + "/" + id);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(data instanceof JSONObject ? new Invoice((JSONObject) data) : null);
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return MockProvider.invoice();
    }
}
