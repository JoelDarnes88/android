package org.udg.pds.todoandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.databinding.ForgotPasswordBinding;
import org.udg.pds.todoandroid.rest.TodoApi;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    TodoApi mTodoService;
    private ForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTodoService = ((TodoApp) getApplication()).getAPI();

        binding.forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.forgotPasswordEmail.getText().toString();
                // Aquí puedes añadir más campos según tus necesidades

                forgotPassword(email);
            }
        });
    }

    private void forgotPassword(String email) {
        // RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), email);
        Call<Void> call = mTodoService.forgotPassword(email);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Toast.makeText(ForgotPassword.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Correu enviat", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(ForgotPassword.this, "Error en enviar el correu: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ForgotPassword.this, "Error en la sol·licitut del correu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
