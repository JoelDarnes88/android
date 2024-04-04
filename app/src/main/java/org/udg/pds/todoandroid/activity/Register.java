package org.udg.pds.todoandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.databinding.RegisterBinding;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.entity.UserRegister;
import org.udg.pds.todoandroid.rest.TodoApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    TodoApi mTodoService;
    private RegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTodoService = ((TodoApp) getApplication()).getAPI();

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.registerUsername.getText().toString();
                String password = binding.registerPassword.getText().toString();
                String email = binding.registerEmail.getText().toString();
                // Aquí puedes añadir más campos según tus necesidades

                registerUser(username, password, email);
            }
        });
    }

    private void registerUser(String username, String password, String email) {
        UserRegister userRegister = new UserRegister(username, password, email);
        Call<User> call = mTodoService.register(userRegister);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(Register.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(Register.this, "Error registering user: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Register.this, "Error registering user, failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
