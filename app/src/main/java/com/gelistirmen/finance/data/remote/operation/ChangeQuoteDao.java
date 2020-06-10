package com.gelistirmen.finance.data.remote.operation;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.ChangeQuoteRequest;
import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeQuoteDao extends FMDao {

    public ChangeQuoteDao(VolleyDaoListener listener, ChangeQuoteRequest request) {
        super(listener, Constants.ApiMethod.ChangeQuote, Request.Method.PUT, true);
        JSONObject data = new JSONObject();
        try {
            data.put("id", request.quoteId);
            data.put("cRNotes", request.note);
            data.put("requestedMaturityDate", Method.getFormatedDate(request.requestedMaturityDate, Constants.webServiceDateTimeFormat));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.bodyDataObject = data;
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest();
    }
}
