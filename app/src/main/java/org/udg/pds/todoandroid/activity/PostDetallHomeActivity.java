package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.fragment.HomePostsFragment;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetallHomeActivity extends AppCompatActivity {

    TodoApi mApiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detall_home);
        mApiService = ((TodoApp) this.getApplication()).getAPI();


        this.crearHomePost();

    }

    private void crearHomePost(){

        String postId = getIntent().getStringExtra("POST_ID");
        Call<Post> call = mApiService.getPostId(postId);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    PostDetallHomeActivity.this.showPost(response.body());
                } else {
                    Toast.makeText(PostDetallHomeActivity.this, "Error al obtenir els detalls del post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostDetallHomeActivity.this, "Fallada al obtenir les dades del post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPost(Post p) {
        TextView titol = findViewById(R.id.tvTitle);
        TextView descripcio = findViewById(R.id.tvDescription);
        TextView preu = findViewById(R.id.tvPrice);
        TextView userPropietari = findViewById(R.id.tvUserName);
        User u = p.getUser();
        Long userID = p.getUserId();
        titol.setText(p.getTitol());
        descripcio.setText(p.getDescripcio());
        preu.setText(String.format(Locale.getDefault(), "$%.2f", p.getPreu()));
        userPropietari.setText(u.getName());

    }

}
