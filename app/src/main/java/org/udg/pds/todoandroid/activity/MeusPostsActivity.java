package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.databinding.ActivityMeusPostsBinding;
import org.udg.pds.todoandroid.databinding.FragmentHomePostsBinding;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.fragment.HomePostsFragment;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeusPostsActivity extends AppCompatActivity {

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

    public void updatePostList() {
        Call<List<Post>> call = mTodoService.getMeusPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    showPostsList(response.body());
                } else {
                    Toast.makeText(MeusPostsActivity.this, "Error llegint els posts", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("MeusPostsActivity", "Error al cargar los posts", t);
                Toast.makeText(MeusPostsActivity.this, "Error en la red", Toast.LENGTH_LONG).show();
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
        TextView description;
        TextView price;

        PostsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.post_item_titol);
            description = itemView.findViewById(R.id.post_item_descripcio);
            price = itemView.findViewById(R.id.post_item_preu);
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
            holder.description.setText(post.getDescripcio());
            holder.price.setText(String.valueOf(post.getPreu()));

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(MeusPostsActivity.this, MeusPostsDetallActivity.class);
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
