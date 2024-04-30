package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.adapter.ImagesAdapter;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeusPostsDetallActivity extends AppCompatActivity {

    TodoApi mApiService;
    private static final int REQUEST_EDIT_POST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_posts_detall);
        mApiService = ((TodoApp) this.getApplication()).getAPI();


        findViewById(R.id.boto_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });

        findViewById(R.id.boto_editar).setOnClickListener(new View.OnClickListener() {
            String postId = getIntent().getStringExtra("POST_ID");
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeusPostsDetallActivity.this, EditarPostActivity.class);
                intent.putExtra("POST_ID", postId);
                startActivityForResult(intent,REQUEST_EDIT_POST);
            }
        });
        this.carregarElsMeusPosts();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_POST && resultCode == RESULT_OK) {
            carregarElsMeusPosts();
        }
    }

    private void carregarElsMeusPosts(){

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
        ViewPager2 viewPager = findViewById(R.id.meus_post_item_image);

        titol.setText(p.getTitol());
        descripcio.setText(p.getDescripcio());
        preu.setText(String.format(Locale.getDefault(), "$%.2f", p.getPreu()));

        ImagesAdapter adapter = new ImagesAdapter(this, p.getImages());
        viewPager.setAdapter(adapter);
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
