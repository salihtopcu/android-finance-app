package com.gelistirmen.finance.data.remote.settings;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.settings.City;

import org.json.JSONArray;

public class CitiesDao extends FMDao {

    public CitiesDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.Cities);
        super.setRequestType(RequestType.JsonArrayRequest);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        City.List cities = null;
        if (data instanceof JSONArray)
            cities = new City.List((JSONArray) data);
        super.onAfterSuccessRequest(cities);
    }
}
