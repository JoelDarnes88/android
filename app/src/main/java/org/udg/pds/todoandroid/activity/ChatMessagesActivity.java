package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.adapter.MessageAdapter;
import org.udg.pds.todoandroid.entity.Message;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private TodoApi mTodoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        recyclerView = findViewById(R.id.messages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Long chatId = getIntent().getLongExtra("CHAT_ID", -1);
        if (chatId == -1) {
            Toast.makeText(this, "Invalid Chat ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        mTodoService = ((TodoApp) getApplication()).getAPI();
        loadMessages(chatId);
    }

    private void loadMessages(Long chatId) {
        Call<List<Message>> call = mTodoService.getChatMessages(chatId);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    adapter = new MessageAdapter(ChatMessagesActivity.this, response.body());
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(ChatMessagesActivity.this, "Fallada carregant missatges", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(ChatMessagesActivity.this, "Error connexió", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
