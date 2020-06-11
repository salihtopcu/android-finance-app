package com.gelistirmen.finance.data.remote.membership;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MockProvider;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.UserProfile;

import org.json.JSONObject;

public class UserProfileDao extends FMDao {

    public UserProfileDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.Me, Request.Method.GET, true);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(new UserProfile((JSONObject) data));
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return MockProvider.userProfile();
    }
}
