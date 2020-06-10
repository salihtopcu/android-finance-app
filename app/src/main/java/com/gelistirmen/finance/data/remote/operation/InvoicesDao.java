package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Invoice;

import org.json.JSONArray;

public class InvoicesDao extends FMDao {

    public InvoicesDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.Invoices);
        super.setRequestType(RequestType.JsonArrayRequest);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        Invoice.List invoices = null;
        if (data instanceof JSONArray)
            invoices = new Invoice.List((JSONArray) data);
        super.onAfterSuccessRequest(invoices);
    }
}
