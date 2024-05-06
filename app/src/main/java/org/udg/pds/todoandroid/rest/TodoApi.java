package org.udg.pds.todoandroid.rest;

import org.udg.pds.todoandroid.entity.IdObject;
import org.udg.pds.todoandroid.entity.PaymentResponse;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.entity.UserModify;
import org.udg.pds.todoandroid.entity.UserRegister;
import org.udg.pds.todoandroid.entity.UserLogin;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.entity.Servei;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by imartin on 13/02/17.
 */
public interface TodoApi {
    @POST("/users/login")
    Call<User> login(@Body UserLogin login);

    @POST("/users/register")
    Call<Void> register(@Body UserRegister register);

    @POST("/users/forgotPassword")
    Call<Void> forgotPassword(@Body String email);

    @GET("/users/search")
    Call<List<User>> searchUser(@Query("query") String query);

    @GET("/users/check")
    Call<String> check();

    @GET("/users/getPaymentMethod")
    Call<PaymentResponse> getPaymentMethod();

    @POST("/users/modify")
    Call<Void> modify(@Body UserModify modify);

    @POST("/users/changeFavourites/{post_id}")
    Call<Void> changeFavourite(@Path("post_id") Long post_id, @Body Boolean addToFavourites);

    @GET("/users/isFavourite/{post_id}")
    Call<Boolean> isFavourite(@Path("post_id") Long post_id);

    @DELETE("/users/{id}")
    Call<Void> deleteAccount(@Path("id") String userId);

    @POST("/images")
    @Multipart
    Call<String> uploadImage(@Part MultipartBody.Part file);

    @GET("/posts")
    Call<List<Post>> getPosts();

    @GET("/posts/search")
    Call<List<Post>> getPostSearch(@Query("query") String query);

    @GET("/posts/{id}")
    Call<Post> getPostId(@Path("id") String id);

    @GET("/posts/user/{userId}")
    Call<List<Post>> getUserPosts(@Path("userId") String userId);

    @GET("/posts/user/{userId}/servei/{serviceId}")
    Call<List<Post>> getUserServicePosts(@Path("userId") String userId, @Path("serviceId") String serviceId);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") String postId);

    @Multipart
    @POST("/posts/postImage")
    Call<ResponseBody> addPostImages(@Part("titol") RequestBody titol,
                                     @Part("descripcio") RequestBody descripcio,
                                     @Part("preu") RequestBody preu,
                                     @Part("nomServei") RequestBody nomServei,
                                     @Part List<MultipartBody.Part> files);

    @Multipart
    @PUT("/posts/updatePostImage/{postId}")
    Call<ResponseBody> updatePostImage(
        @Path("postId") String postId,
        @Part("titol") RequestBody titol,
        @Part("descripcio") RequestBody descripcio,
        @Part("preu") RequestBody preu,
        @Part("nomServei") RequestBody nomServei,
        @Part("urlsToDel") List<String> urlsToDel,
        @Part List<MultipartBody.Part> files);

    @GET("/services")
    Call<List<Servei>> getServices();

    @GET("/services/user/{userId}")
    Call<List<Servei>> getServicesUser(@Path("userId") String userId);
}

