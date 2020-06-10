package com.gelistirmen.finance.data.remote.membership;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.local.Cache;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.RenewTokenResponse;

import org.json.JSONObject;

public class RenewTokenDao extends FMDao {

    public RenewTokenDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.RenewToken + "/" + Cache.getRefreshToken(), Request.Method.GET, false);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(new RenewTokenResponse((JSONObject) data));
    }
}
