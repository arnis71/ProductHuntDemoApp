package ru.arnis.producthuntdemoapp.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by arnis on 26/01/2017.
 */

public class UpdateDataCallback extends ResultReceiver {
    static final String TAG = "result_receiver";

    private Receiver receiver;

    private UpdateDataCallback(Handler handler) {
        super(handler);
    }

    private void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public static <T extends Receiver> UpdateDataCallback create(T requester){
        UpdateDataCallback receiver = new UpdateDataCallback(new Handler());
        receiver.setReceiver(requester);
        return receiver;
    }

    void put(Intent intent){
        intent.putExtra(TAG,this);
    }

    public void release(){
        if (receiver!=null)
            setReceiver(null);
    }

    public interface Receiver{
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver!=null)
            receiver.onReceiveResult(resultCode,resultData);
    }
}
