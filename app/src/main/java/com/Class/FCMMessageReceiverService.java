package com.Class;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.emasjid.Login;
import com.emasjid.MasjidDetail_userside;
import com.emasjid.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMMessageReceiverService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private Context mContext = this;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String NOTIFICATION_CHANNEL_NAME = "emasjid";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        Log.d("Msg", "Message received [" + remoteMessage + "]");


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        //Check if the message contains notification
        if (remoteMessage.getData().get("message") != null && remoteMessage.getData().get("message").length() > 0) {
            Log.d(TAG, "Message body:" + remoteMessage.getData().get("message"));

            createNotification(remoteMessage.getData().get("message"),remoteMessage.getData().get("masjid_id"));
        } else {
            createNotification("","");
        }


    }

    private void sendNotification(String body,String id) {


        Intent intent = null;
        intent = new Intent(this, MasjidDetail_userside.class);
        intent.putExtra("masjidid",id);
        intent.putExtra("masjidstatus","user");

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Resources r = this.getResources();
        int resourceId = r.getIdentifier("login_pro", "raw", this.getPackageName());

        Log.e("noti icon",resourceId+"");

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.login_pro)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.login_pro))
                .setContentTitle("eMasjid")
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent).setSound(uri);



        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());


    }


    private void createNotification(String body,String id) {

        Intent intent = null;
        intent = new Intent(this, MasjidDetail_userside.class);
        intent.putExtra("masjidid",id);
        intent.putExtra("masjidstatus","user");

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Resources r = this.getResources();
        int resourceId = r.getIdentifier("login_pro", "raw", this.getPackageName());

        Log.e("noti icon",resourceId+"");

        PendingIntent resultPendingIntent = PendingIntent.getActivity( this,
                1520, intent,
                PendingIntent.FLAG_UPDATE_CURRENT );

        mBuilder = new NotificationCompat.Builder( this );
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            mBuilder.setPriority( NotificationCompat.PRIORITY_MAX );
        }
        mBuilder.setSmallIcon( R.mipmap.ic_launcher );
        mBuilder  .setContentTitle("eMasjid")
                .setContentText(body)
                .setColor( 101 )
                .setNumber( 3 )
                .setAutoCancel( true )
                .setBadgeIconType( R.mipmap.ic_launcher )
                .setContentIntent( resultPendingIntent );

        mNotificationManager = (NotificationManager) mContext.getSystemService( Context.NOTIFICATION_SERVICE );

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance );
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor( Color.RED );
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID );
            mNotificationManager.createNotificationChannel( notificationChannel );
        }
        mNotificationManager.notify( 1520, mBuilder.build() );

    }

}
