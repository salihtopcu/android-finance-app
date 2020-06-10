package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.FactoringProcess;

import org.json.JSONArray;

public class ActiveFactoringProcessesDao extends FMDao {

    public ActiveFactoringProcessesDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.ActiveFactoringProcesses);
        super.setRequestType(RequestType.JsonArrayRequest);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        FactoringProcess.List factoringProcesses = null;
        if (data instanceof JSONArray)
            factoringProcesses = new FactoringProcess.List((JSONArray) data);
        super.onAfterSuccessRequest(factoringProcesses);
    }
}
