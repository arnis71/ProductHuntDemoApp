package ru.arnis.producthuntdemoapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.arnis.producthuntdemoapp.model.CategoryList;
import ru.arnis.producthuntdemoapp.model.PostList;

/**
 * Created by arnis on 24/01/2017.
 */

public class ProductHuntClient {
    private static final String API_KEY = "591f99547f569b05ba7d8777e2e0824eea16c440292cce1f8dfb3952cc9937ff";
    private static final String API_BASE_URL = "https://api.producthunt.com/";

    private Retrofit retrofit;

    public static ProductHuntClient getClient(){
        ProductHuntClient client = new ProductHuntClient();
        client.init();
        return client;
    }
    private ProductHuntClient() {
    }
    private void init(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //For ActiveAndroid and Retrofit compatibility
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Call<CategoryList> getCategories(){
        API api = retrofit.create(API.class);
        return api.getCategories(API_KEY);
    }

    public Call<PostList> getPosts(String name){
        API api = retrofit.create(API.class);
        return api.getPosts(name, API_KEY);
    }
}
