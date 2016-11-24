package com.zzu.ehome;

import android.content.Context;
import android.net.Uri;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Administrator on 2016/9/2.
 */
public class DemoNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        pushNotificationMessage.getPushData();
        pushNotificationMessage.getExtra();
        pushNotificationMessage.getTargetId();

        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        pushNotificationMessage.getPushContent();
        pushNotificationMessage.getPushData();
        return false;

    }
}
