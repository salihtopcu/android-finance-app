package com.gelistirmen.finance.data.remote.operation;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MockProvider;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class InvoiceVisionDao extends FMDao {
    private byte[] invoicePictureData;

    public InvoiceVisionDao(VolleyDaoListener listener, byte[] invoicePictureData) {
        super(listener, Constants.ApiMethod.InvoiceVision, Request.Method.POST, true);
        this.invoicePictureData = invoicePictureData;
        super.setRequestType(RequestType.MultipartRequest);
//        super.setContentType("application/x-www-form-urlencoded");
        super.setMultipartData(invoicePictureData, "invoice", "invoice_test.jpeg", "image/jpeg");
        super.timeout = 60000;
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        JSONObject jsonObject = (JSONObject) data;
        try {
            jsonObject.put("invoiceNo", Method.getString(jsonObject, "serialNo"));
            jsonObject.put("serialNo", Method.getString(jsonObject, "serial"));
            jsonObject.put("invoiceAmount", Method.getDouble(jsonObject, "amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Invoice invoice = new Invoice((JSONObject) data);
        invoice.pictureData = invoicePictureData;
        super.onAfterSuccessRequest(invoice);
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return MockProvider.invoice();
    }
}