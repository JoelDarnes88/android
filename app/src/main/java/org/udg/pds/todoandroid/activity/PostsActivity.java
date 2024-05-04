package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.databinding.ActivityMeusPostsBinding;
import org.udg.pds.todoandroid.databinding.FragmentHomePostsBinding;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.fragment.HomePostsFragment;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity {
    public static final String SHARED_PREFS_KEY = "loggedUser";

    TodoApi mTodoService;
    RecyclerView recyclerView;
    PostsAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ActivityMeusPostsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_posts);

        mTodoService = ((TodoApp) getApplication()).getAPI();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.MeusPostsRecyclerView);

        adapter = new PostsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            updatePostList();
        });

        updatePostList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePostList();
    }

    public void updatePostList() {
        String userId;
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        if(intent != null && intent.hasExtra("userId")) userId = (String) intent.getSerializableExtra("userId");
        else userId = sharedPreferences.getString("id", "");

        Call<List<Post>> call = null;
        if(intent != null && intent.hasExtra("serviceId")) call = mTodoService.getUserServicePosts(userId, (String) intent.getSerializableExtra("serviceId"));
        else call = mTodoService.getUserPosts(userId);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> posts = response.body();
                    adapter.setPosts(posts);
                } else {
                    adapter.setPosts(new ArrayList<>());
                    Toast.makeText(PostsActivity.this, "No tens cap post", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("PostsActivity", "Error al cargar los posts", t);
                Toast.makeText(PostsActivity.this, "Error en la red", Toast.LENGTH_LONG).show();
            }
        });

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void showPostsList(List<Post> posts) {
        adapter.clear();
        adapter.addAll(posts);
    }

    static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView imageView;
        PostsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.post_item_titol);
            price = itemView.findViewById(R.id.post_item_preu);
            imageView = itemView.findViewById(R.id.post_item_image);
        }
    }


    class PostsAdapter extends RecyclerView.Adapter<PostsViewHolder> {
        private List<Post> posts = new ArrayList<>();
        private LayoutInflater inflater;

        PostsAdapter(AppCompatActivity context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.post_item, parent, false);
            return new PostsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PostsViewHolder holder, int position) {
            Post post = posts.get(position);
            holder.title.setText(post.getTitol());
            holder.price.setText(String.valueOf(post.getPreu()));

            if (!post.getImages().isEmpty()) {
                Picasso.get().load(post.getImages().get(0)).into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.all_posts_logo);
            }

            holder.itemView.setOnClickListener(view -> {
                Intent intent;
                Intent intent1 = getIntent();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                if(intent1 != null && intent1.hasExtra("userId") && !Objects.equals((String) intent1.getSerializableExtra("userId"), sharedPreferences.getString("id", ""))) intent = new Intent(PostsActivity.this, PostDetallHomeActivity.class);
                else intent = new Intent(PostsActivity.this, MeusPostsDetallActivity.class);
                intent.putExtra("POST_ID", String.valueOf(post.getId()));
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        void setPosts(List<Post> newPosts) {
            this.posts.clear();
            this.posts.addAll(newPosts);
            notifyDataSetChanged();
        }


        public void addAll(List<Post> newPosts) {
            int initialSize = posts.size();
            posts.addAll(newPosts);
            notifyItemRangeInserted(initialSize, newPosts.size());
        }

        public void clear() {
            int size = posts.size();
            posts.clear();
            notifyItemRangeRemoved(0, size);
        }
    }
}
