package ru.arnis.producthuntdemoapp.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.List;

import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.model.Category;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.service.RequestUpdateData;
import ru.arnis.producthuntdemoapp.service.UpdateDataCallback;
import ru.arnis.producthuntdemoapp.service.UpdateDataService;

public class MainActivity extends AppCompatActivity implements UpdateDataCallback.Receiver {

    private UpdateDataCallback receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setAlarm();
        checkAlarm();

        receiver = UpdateDataCallback.create(this);
        UpdateDataService.startInApp(this,receiver,"books");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.release();
    }

    private List<Category> getCategories() {
        return new Select()
                .from(Category.class)
                .execute();
    }

    private List<Post> getPosts() {
        return new Select()
                .from(Post.class)
//                .where("Thumbnail = ?", thumbnail.getId())
                .execute();
    }

    private void setAlarm(){
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, RequestUpdateData.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long interval = 20000;// * 60 * 120;
        long jitter = 0;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }
    private boolean checkAlarm(){
        boolean alarmUp =  (PendingIntent.getBroadcast(this, 0,
                new Intent(this,RequestUpdateData.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        Toast.makeText(this, "Alarm up "+String.valueOf(alarmUp), Toast.LENGTH_SHORT).show();
        return alarmUp;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Toast.makeText(this, "Update category "+ resultData.getString(UpdateDataService.CATEGORY), Toast.LENGTH_SHORT).show();
    }
}
