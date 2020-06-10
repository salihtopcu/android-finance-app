package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;

public class DebtorContractDao extends FMDao {

    public DebtorContractDao(VolleyDaoListener listener, String id) {
        super(listener, Constants.ApiMethod.DebtorContract.replace("{id}", id));
        super.setRequestType(RequestType.DownloadRequest);
    }
}
