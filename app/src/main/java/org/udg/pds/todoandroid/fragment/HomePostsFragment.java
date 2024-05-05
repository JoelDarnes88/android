package org.udg.pds.todoandroid.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.activity.PostDetallHomeActivity;
import org.udg.pds.todoandroid.databinding.FragmentHomePostsBinding;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePostsFragment extends Fragment {

    TodoApi mTodoService;
    RecyclerView recyclerView;
    private PostsAdapter adapter;
    FragmentHomePostsBinding binding;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = FragmentHomePostsBinding.inflate(inflater, container, false);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updatePostList("");
            }
        });
        binding.backToFeed.setVisibility(View.GONE);
        setupSearchViewAndBackArrow();

        return binding.getRoot();

    }

    private void setupSearchViewAndBackArrow() {
        androidx.appcompat.widget.SearchView searchView = binding.searchViewHomePosts;
        ImageView backToFeed = binding.backToFeed;

        binding.searchViewHomePosts.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updatePostList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    updatePostList("");
                    backToFeed.setVisibility(View.GONE);
                } else {
                    backToFeed.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        backToFeed.setOnClickListener(view -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            updatePostList("");
            backToFeed.setVisibility(View.GONE);
        });
    }

    @Override
    public void onStart() {

        super.onStart();
        mTodoService = ((TodoApp) this.getActivity().getApplication()).getAPI();

        recyclerView = binding.PostsRecyclerView;
        adapter = new HomePostsFragment.PostsAdapter(this.getActivity().getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updatePostList("");
    }

    public void showPostsList(List<Post> pl) {
        adapter.clear();
        for (Post p : pl) {
            adapter.add(p);
        }
    }


    public void updatePostList(String query) {

        Call<List<Post>> call;

        if (query.isEmpty()) {
            call = mTodoService.getPosts();
        } else {
            call = mTodoService.getPostSearch(query);
        }

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    HomePostsFragment.this.showPostsList(response.body());
                } else {
                    Toast.makeText(HomePostsFragment.this.getContext(), "No hi ha cap post", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("HomePostsFragment", "Error al cargar los posts", t);
                Toast.makeText(HomePostsFragment.this.getContext(), "Fallada: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        if (binding.swipeRefreshLayout.isRefreshing()) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }

    }

    static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView titol;
        TextView descripcio;
        TextView preu;
        TextView usuari;
        View view;
        ImageView imageView;

        PostsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titol = itemView.findViewById(R.id.post_item_titol);
            preu = itemView.findViewById(R.id.post_item_preu);
            imageView = itemView.findViewById(R.id.post_item_image);
        }
    }

    static class PostsAdapter extends RecyclerView.Adapter<HomePostsFragment.PostsViewHolder> {

        List<Post> list = new ArrayList<>();
        Context context;

        public PostsAdapter(Context context) {
            this.context = context;
        }

        @Override
        public HomePostsFragment.PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
            HomePostsFragment.PostsViewHolder holder = new HomePostsFragment.PostsViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(HomePostsFragment.PostsViewHolder holder, final int position) {

            holder.titol.setText(list.get(position).titol);
            holder.preu.setText(String.format("€%.2f", list.get(position).preu));
            Post post = list.get(position);

            if (post.getImages() != null && !post.getImages().isEmpty()) {
                Picasso.get().load(post.getImages().get(0)).into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.painting);
            }

            holder.view.setOnClickListener(view -> {
                Intent intent = new Intent(context, PostDetallHomeActivity.class);
                intent.putExtra("POST_ID", String.valueOf(post.getId()));
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            });

            /*
            holder.titol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, holder.titol.getText(), duration);
                    toast.show();
                }
            });
            */
            // animate(holder);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {

            super.onAttachedToRecyclerView(recyclerView);
        }

        public void insert(int position, Post post) {
            list.add(position, post);
            notifyItemInserted(position);
        }

        public void remove(Post post) {
            int position = list.indexOf(post);
            list.remove(position);
            notifyItemRemoved(position);
        }

        public void animate(RecyclerView.ViewHolder viewHolder) {
            final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipate_overshoot_interpolator);
            viewHolder.itemView.setAnimation(animAnticipateOvershoot);
        }

        public void add(Post p) {
            list.add(p);
            this.notifyItemInserted(list.size() - 1);
        }

        public void clear() {
            int size = list.size();
            list.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }
}
