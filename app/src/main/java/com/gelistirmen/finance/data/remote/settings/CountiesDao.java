package com.gelistirmen.finance.data.remote.settings;

import android.support.annotation.Nullable;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.MockProvider;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.settings.City;
import com.gelistirmen.finance.model.settings.County;

import org.json.JSONArray;

public class CountiesDao extends FMDao {

    public CountiesDao(VolleyDaoListener listener, City city) {
        super(listener, Constants.ApiMethod.Counties + "/" + city.id);
        super.setRequestType(RequestType.JsonArrayRequest);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        County.List counties = null;
        if (data instanceof JSONArray)
            counties = new County.List((JSONArray) data);
        super.onAfterSuccessRequest(counties);
    }

    @Nullable
    @Override
    protected Object getMockData() {
        return MockProvider.counties();
    }
}
