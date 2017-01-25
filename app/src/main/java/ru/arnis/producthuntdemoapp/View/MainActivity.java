package ru.arnis.producthuntdemoapp.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.arnis.producthuntdemoapp.service.PostUpdateReceiver;
import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.model.Category;
import ru.arnis.producthuntdemoapp.model.CategoryList;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.model.PostList;
import ru.arnis.producthuntdemoapp.network.ProductHuntClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setAlarm();
        checkAlarm();

        ProductHuntClient client = ProductHuntClient.getClient();

        client.getPosts("tech").enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList body = response.body();
                savePosts(body);
                Log.d("happy", "Posts loaded successfully");

                List<Post> posts = getPosts();
                Log.d("happy", "Posts loaded from db");
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Log.d("happy", "Posts load Error");
            }
        });


        client.getCategories().enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                CategoryList body = response.body();
                Log.d("happy", "Categories loaded successfully");
                saveCategories(body);

                List<Category> all = getCategories();
                Log.d("happy", "Categories loaded from db");
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                Log.d("happy", "Categories load error");
            }
        });

    }

    private void saveCategories(CategoryList categoryList) {
        ActiveAndroid.beginTransaction();
        try {
            for (Category category: categoryList.getCategories()) {
                category.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.d("happy", "Categories saved to db");
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
    private List<Category> getCategories() {
        return new Select()
                .from(Category.class)
                .execute();
    }

    private void savePosts(PostList postList) {
        ActiveAndroid.beginTransaction();
        try {
            for (Post post: postList.getPosts()) {
                post.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.d("happy", "Posts saved to db");
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
    private List<Post> getPosts() {
        return new Select()
                .from(Post.class)
//                .where("Thumbnail = ?", thumbnail.getId())
                .execute();
    }

    private void setAlarm(){
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, PostUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long interval = 1000 * 60 * 120;
        long jitter = 0;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, jitter/*System.currentTimeMillis() + interval*/, interval, pendingIntent);
    }
    private boolean checkAlarm(){
        boolean alarmUp =  (PendingIntent.getBroadcast(this, 0,
                new Intent(this,PostUpdateReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        Toast.makeText(this, "Alarm up "+String.valueOf(alarmUp), Toast.LENGTH_SHORT).show();
        return alarmUp;
    }
}
