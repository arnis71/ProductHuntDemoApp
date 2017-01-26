package ru.arnis.producthuntdemoapp.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.adapter.PagerAdapter;
import ru.arnis.producthuntdemoapp.model.Category;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.service.RequestUpdateData;
import ru.arnis.producthuntdemoapp.service.UpdateDataCallback;
import ru.arnis.producthuntdemoapp.service.UpdateDataService;

public class MainActivity extends AppCompatActivity implements UpdateDataCallback.Receiver {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.container)
    ViewPager viewPager;

    private PagerAdapter pagerAdapter;
    UpdateDataCallback receiver;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("ProductHunt Demo");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        displayData(null);


        setAlarm();
        checkAlarm();

        receiver = UpdateDataCallback.create(this);
        UpdateDataService.startInApp(this,receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.release();
    }

    private void displayData(String explicitCat){
        if (explicitCat==null) {
            categories = getCategories();
            List<List<Post>> posts = new ArrayList<>();
            for (Category category : categories)
                posts.add(getPosts(category));

            pagerAdapter.addData(categories, posts);
        } else {
            for (Category category:categories)
                if (category.getSlug().equals(explicitCat))
                    pagerAdapter.updateData(explicitCat,getPosts(category));
        }
    }

    private List<Category> getCategories() {
        return new Select()
                .from(Category.class)
                .orderBy("catID ASC")
                .execute();
    }

    private List<Post> getPosts(Category category) {
        return new Select()
                .from(Post.class)
                .where("fromCategory = ?", category.getCatID())
                .execute();
    }

    private void setAlarm(){
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, RequestUpdateData.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long interval = 1000 * 60 * 10;
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
        String explicitCat = resultData.getString(UpdateDataService.CATEGORY);
        displayData(explicitCat);
//        if (explicitCat==null){
//        } else {
//            pagerAdapter.categoryFragmentUpdate(explicitCat);
//        }
    }
}
