package com.gelistirmen.finance.data.remote.operation;

import android.support.annotation.Nullable;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;

public class PayeeContractDao extends FMDao {

    public PayeeContractDao(VolleyDaoListener listener, String id) {
        super(listener, Constants.ApiMethod.PayeeContract.replace("{id}", id));
        super.setRequestType(RequestType.DownloadRequest);
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return null;
    }
}
