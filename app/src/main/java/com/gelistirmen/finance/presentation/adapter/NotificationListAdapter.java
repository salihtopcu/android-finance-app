package com.gelistirmen.finance.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.Notification;
import com.gelistirmen.finance.presentation.view.ContentButtonViewHandler;
import com.gelistirmen.finance.util.Method;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    public NotificationListAdapter(Context context, @NonNull Notification.List notifications) {
        super(context, 0, notifications);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView != null) {
            return ((ContentButtonViewHandler) convertView.getTag()).getView();
        } else {
            Notification notification = getItem(position);
            ContentButtonViewHandler viewHandler;
            if (notification.isNotified)
                viewHandler = new ContentButtonViewHandler(super.getContext(), Method.getFormatedDate(notification.createdDate, Constants.interfaceDateFormat), null, notification.message, R.drawable.ic_star_black, ContentButtonViewHandler.Type.Light, null);
            else
                viewHandler = new ContentButtonViewHandler(super.getContext(), Method.getFormatedDate(notification.createdDate, Constants.interfaceDateFormat), null, notification.message, R.drawable.ic_star_white, ContentButtonViewHandler.Type.Dark, null);
            convertView = viewHandler.getView();
            convertView.setTag(viewHandler);
            return convertView;
        }
    }
}
