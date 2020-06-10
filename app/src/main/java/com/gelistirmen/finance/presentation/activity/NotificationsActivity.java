package com.gelistirmen.finance.presentation.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.data.remote.base.VolleyDao;
import com.gelistirmen.finance.data.remote.operation.NotificationsDao;
import com.gelistirmen.finance.model.operation.Notification;
import com.gelistirmen.finance.presentation.adapter.NotificationListAdapter;

import butterknife.BindView;

public class NotificationsActivity extends BaseActivity {
    Notification.List notifications;

    @BindView(R.id.listView) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        super.showActionBarBackButton();
        super.setActionBarTitle(R.string.notifications);
        super.hideBackgroundImage();
        super.setMainScrollContent(this.listView);

        new NotificationsDao(this).execute();
        super.showProgressDialog();
    }

    @Override
    public void daoDidSuccess(VolleyDao dao, Object data) {
        this.notifications = (Notification.List) data;
        this.listView.setAdapter(new NotificationListAdapter(this, this.notifications));
        super.hideProgressDialog();
    }
}
