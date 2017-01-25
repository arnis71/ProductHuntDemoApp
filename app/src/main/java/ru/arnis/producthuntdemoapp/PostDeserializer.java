package ru.arnis.producthuntdemoapp;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ru.arnis.producthuntdemoapp.model.Post;

/**
 * Created by arnis on 25/01/2017.
 */

public class PostDeserializer implements JsonDeserializer<Post> {
    @Override
    public Post deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Post post = new Post();
        try{
            //ActiveAndroid Model class not compatible with Gson since Android M
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manualDeserialization(post,json);
            } else
                post = new Gson().fromJson(json, Post.class);

            String screenUrl = json.getAsJsonObject().get("screenshot_url").getAsJsonObject().get("850px").getAsString();
            String thumbUrl = json.getAsJsonObject().get("thumbnail").getAsJsonObject().get("image_url").getAsString();
            post.setThumbnailUrl(thumbUrl);
            post.setScreenshotUrl(screenUrl);

            return post;
        } catch (Exception e){
            e.printStackTrace();
        }

        return post;
    }

    private void manualDeserialization(Post post, JsonElement json) {
        post.setName(json.getAsJsonObject().get("name").getAsString());
        post.setDescription(json.getAsJsonObject().get("tagline").getAsString());
        post.setUpVotes(json.getAsJsonObject().get("votes_count").getAsInt());
        post.setGetItUrl(json.getAsJsonObject().get("redirect_url").getAsString());
    }
}
