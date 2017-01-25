package ru.arnis.producthuntdemoapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by arnis on 25/01/2017.
 */

public class PostUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALARM", Toast.LENGTH_SHORT).show();
        Intent serviceIntent = new Intent(context, PostUpdateService.class);
        context.startService(serviceIntent);
    }
}
