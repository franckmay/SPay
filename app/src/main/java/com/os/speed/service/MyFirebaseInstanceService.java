package com.os.speed.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import com.os.speed.MainActivity;
import com.os.speed.R;
import com.os.speed.broadreceiver.NotificationReceiver;

import static com.os.speed.staticclass.NOTIFICATION_CHANNEL_ID;
import static com.os.speed.staticclass.getBitmapFromURL;

public class MyFirebaseInstanceService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("achilllelelseee","from  "+remoteMessage.getFrom());

      /*  if(remoteMessage.getData().isEmpty()){
            Log.e("achilllelelseee","aplication fermé");
            // showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTag());
           // showNotifications(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getImageUrl().getPath() );
            showNotificationWithPicture();
        }else{
            // showNotification(remoteMessage.getData());
            showNotificationWithPicture();
            Log.e("achilllelelseee","application ouver");
            //showNotifications(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"),remoteMessage.getData().get("avatar_url"));
        }*/
        //showNotification( v);

    }


    private void shwoNOTIFICATION()
    {
        int icon = R.drawable.speed;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, "Custom Notification", when);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.drawable.speed);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");
        notification.contentView = contentView;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound

        mNotificationManager.notify(1, notification);
    }


    public void showNotificationWithPicture(){
        Notification foregroundNote;

        RemoteViews bigView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.notification_expended);

// bigView.setOnClickPendingIntent() etc..

        Notification.Builder mNotifyBuilder = new Notification.Builder(this);
        foregroundNote = mNotifyBuilder.setContentTitle("some string")
                .setContentText("Slide down on note to expand")
                .setSmallIcon(R.drawable.speed)
                //.setLargeIcon(getBitmapFromURL("url"))
                .build();

        foregroundNote.bigContentView = bigView;

// now show notification..
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(1, foregroundNote);
    }




    private void showNotifications(String title, String body,String url ) {
        int value=new Random().nextInt();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // plus petit
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapssed);

        collapsedView.setTextViewText(R.id.text_new_message, title);
        collapsedView.setTextViewText(R.id.text_view_colla, title);
        collapsedView.setImageViewResource(R.id.image_view_expand, R.drawable.speed);




// grand
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expended);
        expandedView.setTextViewText(R.id.text_titre, title);
        if (url != null && !url.isEmpty()) {
            expandedView.setImageViewBitmap(R.id.image_view_expanded,  getBitmapFromURL(url));
        }else{
            expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.speed);
        }



        Intent clickIntent = new Intent(this, NotificationReceiver.class);
        clickIntent.putExtra("vaulue",value);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                value, clickIntent, 0);









        //




        //collapsedView.setBoolean(R.id.sw);


        // expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.speed);

        // gestion des click
        collapsedView.setOnClickPendingIntent(R.id.liner_collaps, clickPendingIntent);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);

            notificationManager.createNotificationChannel(notificationChannel);
        }



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                // .setDefaults(Notification.DEFAULT_ALL)
                //.setWhen(System.currentTimeMillis())

                // .setContentTitle(title)
                //.setContentText(body)
                //.setSmallIcon(R.drawable.ic_launcher_background)

                .setSmallIcon(R.drawable.ic_add)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentInfo("info");

        //  notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        notificationManager.notify(1, notificationBuilder.build());





    }

      private void showNotification(Map<String, String> data) {
          String title = data.get("title").toString();
          String body = data.get("body").toString();
          String url = data.get("avatar_url").toString();
          NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
          String NOTIFICATION_CHANNEL_ID = "com.os.wintime.service.text";
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                      NotificationManager.IMPORTANCE_DEFAULT);
              notificationChannel.setDescription("code sphere");
              notificationChannel.enableLights(true);
              notificationChannel.setLightColor(Color.BLUE);
              notificationChannel.enableVibration(true);
              notificationManager.createNotificationChannel(notificationChannel);
          }
          NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
          notificationBuilder.setAutoCancel(true)
                  .setDefaults(Notification.DEFAULT_ALL)
                  .setWhen(System.currentTimeMillis())
                  .setSmallIcon(R.drawable.speed)
                  .setContentTitle(title)
                  .setContentText(body)
                  .setContentInfo("info");
          notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
      }

      private void showNotification(String title, String body,String tag ) {
          NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
          String NOTIFICATION_CHANNEL_ID = "com.os.wintime.service.text";
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                      NotificationManager.IMPORTANCE_DEFAULT);
              notificationChannel.setDescription("code sphere");
              notificationChannel.enableLights(true);
              notificationChannel.setLightColor(Color.BLUE);
              notificationChannel.enableLights(true);
              notificationManager.createNotificationChannel(notificationChannel);
          }
          NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
          notificationBuilder.setAutoCancel(true)
                  .setDefaults(Notification.DEFAULT_ALL)
                  .setWhen(System.currentTimeMillis())
                  .setSmallIcon(R.drawable.ic_launcher_background)
                  .setContentTitle(title)
                  .setContentText(body)
                  .setContentInfo("info");
          notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
      }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SharedPreferences preference=this.getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        Log.e("TOkensss ", s);

        SharedPreferences.Editor editor=preference.edit();
        editor.putString("tokenfirebase",s).apply();
        editor.apply();


    }


}
