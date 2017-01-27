package ru.arnis.producthuntdemoapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.os.ResultReceiver;

import com.activeandroid.ActiveAndroid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.model.Category;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.model.PostList;
import ru.arnis.producthuntdemoapp.network.ProductHuntClient;
import ru.arnis.producthuntdemoapp.view.MainActivity;

/**
 * Created by arnis on 25/01/2017.
 */

public class UpdateDataService extends IntentService {
    public static final String CATEGORY = "category";
    public static final String DB = "db";

    public UpdateDataService() {
        super("UpdateDataService");
    }

    public static void startInBackground(Context context){
        Intent serviceIntent = new Intent(context, UpdateDataService.class);
        context.startService(serviceIntent);
    }

    public static void startInApp(Context context, UpdateDataCallback receiver){
        Intent serviceIntent = new Intent(context, UpdateDataService.class);
        if (receiver!=null)
            receiver.put(serviceIntent);
        context.startService(serviceIntent);
    }

    public static void startInApp(Context context, UpdateDataCallback receiver, String category){
        Intent serviceIntent = new Intent(context, UpdateDataService.class);
        if (receiver!=null)
            receiver.put(serviceIntent);
        serviceIntent.putExtra(CATEGORY, category);
        context.startService(serviceIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String explicitCategory = intent.getStringExtra(CATEGORY);

        Map<Category, PostList> data = ProductHuntClient.getClient().getPostsByCategory(explicitCategory);

        SharedPreferences preferences = getSharedPreferences(DB,MODE_PRIVATE);
        Post post = null;
        Map<String,Integer> notificationData = new HashMap<>();

        for (Category category: data.keySet()) {
            List<Post> postList = data.get(category).getPosts();

            int newPosts = postList.size() - preferences.getInt(category.getSlug(), 0);
            preferences.edit().putInt(category.getSlug(),postList.size()).apply();

            if (newPosts == 1 && post == null) {
                post = data.get(category).getLast();
            } else if (newPosts>1)
                notificationData.put(category.getSlug(), newPosts);
        }

        writeToDB(data);

        if (!activityCallback(intent, explicitCategory))
            sendNotification(notificationData, post);
    }

    private void writeToDB(Map<Category, PostList> data){
        ActiveAndroid.beginTransaction();
        try{
            for (Category category :data.keySet()){
                List<Post> list = data.get(category).getPosts();
                if (list.size()!=0)
                    for (Post post: data.get(category).getPosts())
                        post.save();
                category.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
        ActiveAndroid.endTransaction();
        }
    }

    private boolean activityCallback(Intent intent, String explicitCategory){
        ResultReceiver receiver = intent.getParcelableExtra(UpdateDataCallback.TAG);

        if (receiver!=null){
            Bundle bundle = new Bundle();
            bundle.putString(CATEGORY,explicitCategory);
            receiver.send(0, bundle);
        }

        return receiver!=null;
    }

    private void sendNotification(Map<String,Integer> notificationData, Post onlyPost){
        if (notificationData.size()==1&&notificationData.values().iterator().next()==1)
            notifySingle(onlyPost);
        else //if (notificationData.size()>0)
            notifyMultiple(notificationData);
    }

    private void notifySingle(Post post){
        NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("В категории "+ post.getName()+" появился новый пост")
                .setContentText(post.getDescription())
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLights(Color.RED,1000,1000)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        manager.notify(1,builder.build());
    }

    private void notifyMultiple(Map<String,Integer> newPosts){
        NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Новые посты:");

        for (String category: newPosts.keySet()){
            inboxStyle.addLine("В категории "+ category +" появилось "+ newPosts.get(category) +" постов");
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("В нескольких категоряих появились новые посты")
                //.setContentText(post.getDescription())
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLights(Color.RED,1000,1000)
                .setAutoCancel(true)
                .setStyle(inboxStyle)
                .setContentIntent(pendingIntent);

        manager.notify(1,builder.build());
    }
}
