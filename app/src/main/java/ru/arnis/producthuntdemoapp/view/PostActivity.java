package ru.arnis.producthuntdemoapp.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.model.Post;

public class PostActivity extends AppCompatActivity {
    @BindView(R.id.post_title)
    TextView title;
    @BindView(R.id.post_description)
    TextView description;
    @BindView(R.id.post_screenshot)
    ImageView screenshot;
    @BindView(R.id.post_thumbnail)
    ImageView thumbnail;
    @BindView(R.id.get_it)
    Button getIt;
    @BindView(R.id.up_votes)
    Button upVotes;
    @BindColor(R.color.colorAccent)
    int colotAccent;

    private Post post;
    private CustomTabsSession session;

    public static void launch(Context context, String postName){
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("postname",postName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        final PostActivity postActivity = this;
        getPost();

        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                client.warmup(0);
                session = client.newSession(null);
                session.mayLaunchUrl(Uri.parse(post.getGetItUrl()),null,null);
                Toast.makeText(postActivity, "CONNECTION", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        boolean ok = CustomTabsClient.bindCustomTabsService(this,"com.android.chrome",connection);
        Toast.makeText(postActivity, "connection " +String.valueOf(ok), Toast.LENGTH_SHORT).show();

        title.setText(post.getName());
        description.setText(post.getDescription());
        Glide.with(this).load(post.getThumbnailUrl()).into(thumbnail);
        Glide.with(this).load(post.getScreenshotUrl()).into(screenshot);
        upVotes.setText(String.valueOf(post.getUpVotes()));
    }

    @OnClick(R.id.get_it)
    void getIt(View view){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(session);
        builder.setToolbarColor(colotAccent);
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(post.getGetItUrl()));
    }

    private void getPost(){
        String postName = getIntent().getStringExtra("postname");
        if (postName!=null)
            post = (Post) new Select()
                .from(Post.class)
                .where("name = ?", postName)
                .execute()
                .iterator()
                .next();
    }
}
