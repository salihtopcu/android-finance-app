package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;

public class PayeeContractDao extends FMDao {

    public PayeeContractDao(VolleyDaoListener listener, String id) {
        super(listener, Constants.ApiMethod.PayeeContract.replace("{id}", id));
        super.setRequestType(RequestType.DownloadRequest);
    }

}
