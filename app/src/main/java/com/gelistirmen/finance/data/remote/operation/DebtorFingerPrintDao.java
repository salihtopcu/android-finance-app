package com.gelistirmen.finance.data.remote.operation;

import android.support.annotation.Nullable;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MockProvider;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Fingerprint;

import org.json.JSONObject;

public class DebtorFingerPrintDao extends FMDao {

    public DebtorFingerPrintDao(VolleyDaoListener listener, String quoteId) {
        super(listener, Constants.ApiMethod.DebtorFingerPrint.replace("{quoteId}", quoteId));
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(data instanceof JSONObject ? new Fingerprint((JSONObject) data) : null);
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return MockProvider.fingerprint();
    }
}
