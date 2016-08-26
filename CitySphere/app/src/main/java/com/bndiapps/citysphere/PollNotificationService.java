package com.bndiapps.citysphere;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;

/**
 * Created by andrei on 8/26/16.
 */
public class PollNotificationService extends IntentService {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String POLL = "POLL";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PollNotificationService() {
        super("PollNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int notificationId = intent.getExtras().getInt(NOTIFICATION_ID);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
        Poll poll = (Poll) intent.getExtras().getSerializable(POLL);
        if(poll!=null){
            // TODO - POLL wizard
        }
    }
}
