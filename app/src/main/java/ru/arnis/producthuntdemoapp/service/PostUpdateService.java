package ru.arnis.producthuntdemoapp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.model.PostList;
import ru.arnis.producthuntdemoapp.network.ProductHuntClient;

/**
 * Created by arnis on 25/01/2017.
 */

public class PostUpdateService extends IntentService {

    public PostUpdateService() {
        super("PostUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ProductHuntClient client = ProductHuntClient.getClient();
        Map<String, PostList> posts = client.getPostsByCategory();
        SharedPreferences preferences = getSharedPreferences("db",MODE_PRIVATE);

        int totalNewPosts=0;
        Post post = null;
        Map<String,Integer> map = new HashMap<>();

        for (String key: posts.keySet()) {
            List<Post> postList = posts.get(key).getPosts();

            int newPosts = postList.size() - preferences.getInt(key, 0);
            preferences.edit().putInt(key,postList.size()).apply();

            if (newPosts == 1 && post == null) {
                post = posts.get(key).getLast();
            } else if (newPosts>1)
                map.put(key, newPosts);

            totalNewPosts+=newPosts;
        }

        if (totalNewPosts==1){
            notifySingle(post);
        } else if (totalNewPosts>1){
            notifyMultiple(map);
        }
    }

    private void notifySingle(Post post){
        NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("В категории "+ post.getName()+" появился новый пост")
                .setContentText(post.getDescription())
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLights(Color.BLUE,1000,1000)
                .setAutoCancel(true);
                //.setContentIntent(new Intent(getApplicationContext(), MainActivity.class));

        manager.notify(1,builder.build());
    }

    private void notifyMultiple(Map<String,Integer> newPosts){

    }
}
