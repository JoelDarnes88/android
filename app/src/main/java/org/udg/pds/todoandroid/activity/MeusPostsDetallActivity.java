package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeusPostsDetallActivity extends AppCompatActivity {

    TodoApi mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_posts_detall);
        mApiService = ((TodoApp) this.getApplication()).getAPI();


        this.crearMeuPost();


        findViewById(R.id.boto_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });
    }


    private void crearMeuPost(){

        String postId = getIntent().getStringExtra("POST_ID");
        Call<Post> call = mApiService.getPostId(postId);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    MeusPostsDetallActivity.this.showPost(response.body());
                } else {
                    Toast.makeText(MeusPostsDetallActivity.this, "Error al obtenir els detalls del post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(MeusPostsDetallActivity.this, "Fallada al obtenir les dades del post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPost(Post p) {
        TextView titol = findViewById(R.id.tvTitle);
        TextView descripcio = findViewById(R.id.tvDescription);
        TextView preu = findViewById(R.id.tvPrice);

        titol.setText(p.getTitol());
        descripcio.setText(p.getDescripcio());
        preu.setText(String.format(Locale.getDefault(), "$%.2f", p.getPreu()));
    }

    private void deletePost() {
        String postId = getIntent().getStringExtra("POST_ID");
        Call<Void> call = mApiService.deletePost(postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MeusPostsDetallActivity.this, "Post eliminat", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MeusPostsDetallActivity.this, "Error al eliminar el post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MeusPostsDetallActivity.this, "Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
