package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPostActivity extends AppCompatActivity {

    TodoApi mTodoService;
    private EditText editTextTitle, editTextDescription, editTextPrice;
    private String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_post);

        mTodoService = ((TodoApp) getApplication()).getAPI();

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        Button buttonSave = findViewById(R.id.buttonSavePost);


        postId = getIntent().getStringExtra("POST_ID");
        loadPostData(postId);


        if (buttonSave != null) {
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePostChanges(postId);
                }
            });
        } else {
            Log.e("EditarPostActivity", "Initialization error: Save button is not found!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String postId = getIntent().getStringExtra("POST_ID");
        loadPostData(postId);
    }

    private void loadPostData(String postId) {

        Call<Post> call = mTodoService.getPostId(postId);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post post = response.body();
                    editTextTitle.setText(post.getTitol());
                    editTextDescription.setText(post.getDescripcio());
                    editTextPrice.setText(String.valueOf(post.getPreu()));
                } else {
                    Toast.makeText(EditarPostActivity.this, "Fallada obtenint el post", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(EditarPostActivity.this, "Error carregant el post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePostChanges(String postId) {
        String titol = editTextTitle.getText().toString();
        String descripcio = editTextDescription.getText().toString();
        double preu = Double.parseDouble(editTextPrice.getText().toString());

        Call<Void> call = mTodoService.updatePost(postId, new Post(titol, descripcio, preu));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarPostActivity.this, "Post guardat correctament", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditarPostActivity.this, "Fallada guardant el post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditarPostActivity.this, "Error guardant", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
