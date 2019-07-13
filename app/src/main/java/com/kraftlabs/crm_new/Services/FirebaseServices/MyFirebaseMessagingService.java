package com.kraftlabs.crm_new.Services.FirebaseServices;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kraftlabs.crm_new.Activities.MainActivity;
import com.kraftlabs.crm_new.Db.NotificationDB;
import com.kraftlabs.crm_new.Models.NotificationModel;
import com.kraftlabs.crm_new.Util.NotificationUtils;
import com.kraftlabs.crm_new.Util.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by AShik on 12/1/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    public static final String PUSH_NOTIFICATION = "PUSH";
    private NotificationModel notificationModel;
    private NotificationDB notificationDB;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        Intent pushNotification = new Intent(PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();

    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String timestamp = data.getString("timestamp");

            JSONObject payload = data.getJSONObject("payload");
            String fragment = payload.getString("fragment");
            String id = payload.getString("id");

            int userId = payload.getInt("user_id");

            notificationModel=new NotificationModel(
                    1,
                    title,
                    message,
                    fragment,
                    id,
                    timestamp,
                    0
            );
            try {
                notificationDB=new NotificationDB(getApplicationContext());
            } catch (NullPointerException e) {
                notificationDB = new NotificationDB(getBaseContext());

            }
            notificationDB.insert(notificationModel);
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "timestamp: " + timestamp);
            Intent resultIntent = null;
            Context context = getApplicationContext();

            if (PrefUtils.getCurrentUser(context) != null && PrefUtils.getCurrentUser(context).getUserId() ==userId) {
                resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("fragment", fragment);
                resultIntent.putExtra("id", id);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}
/*
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.NotifConfig.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {*/
// app is in background, show the notification in notification tray


// check for image attachment
               /* if (TextUtils.isEmpty(imageUrl)) {*/

         /*       } else {*/
// image is present, show notification with image
//  showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//         }
//   }


/**
 * Showing notification with text only
 * <p>
 * Showing notification with text and image
 * <p>
 * Showing notification with text and image
 * <p>
 * Showing notification with text and image
 * <p>
 * Showing notification with text and image
 * <p>
 * Showing notification with text and image
 */


/**
 * Showing notification with text and image
 */
  /*  private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }*/

