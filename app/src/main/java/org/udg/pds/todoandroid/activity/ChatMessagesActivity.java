package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.adapter.MessageAdapter;
import org.udg.pds.todoandroid.entity.Message;
import org.udg.pds.todoandroid.rest.TodoApi;
import org.udg.pds.todoandroid.util.UserUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private TodoApi mTodoService;
    private Long chatId;
    private EditText editMessage;
    private List<Message> messages = new ArrayList<>();
    private Long senderId;

    private Handler handler;
    private Runnable updateRunnable;
    private static final int UPDATE_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        recyclerView = findViewById(R.id.messages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editMessage = findViewById(R.id.editTextMessage);
        Button buttonSend = findViewById(R.id.buttonSendMessage);

        chatId = getIntent().getLongExtra("CHAT_ID", -1);
        if (chatId == -1) {
            Toast.makeText(this, "Invalid Chat ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        senderId = UserUtils.getCurrentUserId(this);
        if (senderId == null) {
            Toast.makeText(this, "Error obtenint user ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        mTodoService = ((TodoApp) getApplication()).getAPI();
        loadMessages(chatId);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        handler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                loadMessages(chatId);
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateRunnable);
    }


    private void loadMessages(Long chatId) {
        Call<List<Message>> call = mTodoService.getChatMessages(chatId);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    messages = response.body();
                    adapter = new MessageAdapter(ChatMessagesActivity.this, messages);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(messages.size() - 1);
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

    private void sendMessage() {
        String content = editMessage.getText().toString();
        if (content.isEmpty()) {
            Toast.makeText(this, "Escriu alguna cosa!", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Message> call = mTodoService.sendMessage(chatId, senderId, content);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    messages.add(response.body());
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                    editMessage.setText("");
                } else {
                    Toast.makeText(ChatMessagesActivity.this, "Error enviant missatge", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(ChatMessagesActivity.this, "Error de xarxa", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
