package com.gelistirmen.finance.data.remote.operation;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.operation.Notification;

import org.json.JSONArray;

public class NotificationsDao extends FMDao {

    public NotificationsDao(VolleyDaoListener listener) {
        super(listener, Constants.ApiMethod.Notifications);
        super.setRequestType(RequestType.JsonArrayRequest);
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        Notification.List notifications = null;
        if (data instanceof JSONArray)
            notifications = new Notification.List((JSONArray) data);
        super.onAfterSuccessRequest(notifications);
    }
}
