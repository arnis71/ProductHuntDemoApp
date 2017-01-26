package ru.arnis.producthuntdemoapp.network;

import android.support.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.arnis.producthuntdemoapp.PostDeserializer;
import ru.arnis.producthuntdemoapp.model.Category;
import ru.arnis.producthuntdemoapp.model.CategoryList;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.model.PostList;

/**
 * Created by arnis on 24/01/2017.
 */

public class ProductHuntClient {
    private static final String API_KEY = "591f99547f569b05ba7d8777e2e0824eea16c440292cce1f8dfb3952cc9937ff";
    private static final String API_BASE_URL = "https://api.producthunt.com/";

    private static ProductHuntClient client;
    private API api;

    public static ProductHuntClient getClient(){
        if (client==null) {
            client = new ProductHuntClient();
            client.init();
        }
        return client;
    }
    private ProductHuntClient() {
    }
    private void init(){
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Post.class, new PostDeserializer())
                //for ActiveAndroid and Retrofit compatibility
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(API.class);
    }

    private Call<CategoryList> getCategories(){
        return api.getCategories(API_KEY);
    }

    private Call<PostList> getPosts(String cat){
        return api.getPosts(cat, API_KEY);
    }

    @WorkerThread
    public Map<String,PostList> getPostsByCategory(String explicitCategory){
        Map<String,PostList> postsByCat = new HashMap<>();
        try {
            if (explicitCategory!=null){
                postsByCat.put(explicitCategory, getPosts(explicitCategory).execute().body());
            } else {
                CategoryList catList = getCategories().execute().body();
                for (Category category : catList.getCategories()) {
                    PostList postList = getPosts(category.getSlug()).execute().body();
                    postsByCat.put(category.getSlug(), postList);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return postsByCat;
    }
}
