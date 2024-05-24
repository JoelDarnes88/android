package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.adapter.ImagesAdapter;
import org.udg.pds.todoandroid.entity.Chat;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.fragment.HomePostsFragment;
import org.udg.pds.todoandroid.rest.TodoApi;
import org.udg.pds.todoandroid.util.UserUtils;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetallHomeActivity extends AppCompatActivity {

    TodoApi mApiService;

    Long post_id;
    Boolean isFavourite = false;

    Long userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detall_home);
        mApiService = ((TodoApp) this.getApplication()).getAPI();

        userId = UserUtils.getCurrentUserId(this);

        this.crearHomePost();
        findViewById(R.id.favorite_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFavourite(post_id);
            }
        });
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
        ViewPager2 viewPager = findViewById(R.id.post_item_image);
        ImageView favoriteIcon = findViewById(R.id.favorite_icon);
        Button contactButton = findViewById(R.id.buttonContact);

        post_id = p.getId();
        isFavourite(post_id);

        User u = p.getUser();
        if (u.getId().equals(userId)) {
            contactButton.setVisibility(View.GONE);
        } else {
            contactButton.setVisibility(View.VISIBLE);
            contactButton.setOnClickListener(v -> createOrOpenChat(userId, u.getId(), post_id));
        }

        titol.setText(p.getTitol());
        descripcio.setText(p.getDescripcio());
        preu.setText(String.format(Locale.getDefault(), "$%.2f", p.getPreu()));
        userPropietari.setText(u.getName());

        ImagesAdapter adapter = new ImagesAdapter(this, p.getImages());
        viewPager.setAdapter(adapter);
    }

    private void changeFavourite(Long post_id){
        Call<Void> call = mApiService.changeFavourite(post_id, !isFavourite);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ImageView imgButton = (ImageView) PostDetallHomeActivity.this.findViewById(R.id.favorite_icon);
                    if(isFavourite){
                        imgButton.setImageResource(R.drawable.star_not_filled);
                        Toast.makeText(PostDetallHomeActivity.this, "Post tret de preferits", Toast.LENGTH_SHORT).show();
                    } else{
                        imgButton.setImageResource(R.drawable.star_filled);
                        Toast.makeText(PostDetallHomeActivity.this, "Post afegit a preferits", Toast.LENGTH_SHORT).show();
                    }
                    isFavourite = !isFavourite;
                } else {
                    Toast.makeText(PostDetallHomeActivity.this, "Error a l'hora de modificar el post de preferits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PostDetallHomeActivity.this, "Fallada a l'hora de modificar el post de preferits", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isFavourite(Long post_id){
        Call<Boolean> call = mApiService.isFavourite(post_id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    ImageView imgButton = (ImageView) PostDetallHomeActivity.this.findViewById(R.id.favorite_icon);
                    isFavourite = response.body();
                    if(isFavourite) imgButton.setImageResource(R.drawable.star_filled);
                    else imgButton.setImageResource(R.drawable.star_not_filled);
                } else {
                    Toast.makeText(PostDetallHomeActivity.this, "Error a l'hora de saber si el post és de preferits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(PostDetallHomeActivity.this, "Fallada a l'hora de saber si el post és de preferits", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createOrOpenChat(Long userId, Long userTargetId, Long postId) {
        Call<Chat> call = mApiService.createChat(userId, userTargetId, postId);
        call.enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful()) {
                    Chat chat = response.body();
                    Intent intent = new Intent(PostDetallHomeActivity.this, ChatMessagesActivity.class);
                    intent.putExtra("CHAT_ID", chat.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(PostDetallHomeActivity.this, "Error al crear o obtenir el chat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                Toast.makeText(PostDetallHomeActivity.this, "Error de xarxa", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
