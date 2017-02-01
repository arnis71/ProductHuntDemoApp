package ru.arnis.producthuntdemoapp.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.arnis.producthuntdemoapp.model.CategoryList;
import ru.arnis.producthuntdemoapp.model.PostList;

/**
 * Created by arnis on 24/01/2017.
 */

interface API {
    @GET("/v1/categories")
    Call<CategoryList> getCategories(@Query("access_token") String token);

    @GET("/v1/categories/{category}/posts")
    Call<PostList> getPosts(@Path("category") String category, @Query("access_token") String token);
}
