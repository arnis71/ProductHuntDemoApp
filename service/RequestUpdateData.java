package ru.arnis.producthuntdemoapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by arnis on 25/01/2017.
 */

public class RequestUpdateData extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateDataService.startInBackground(context);
    }
}
