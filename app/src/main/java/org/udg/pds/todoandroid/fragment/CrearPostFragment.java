package org.udg.pds.todoandroid.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.udg.pds.todoandroid.entity.IdObject;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.rest.TodoApi;
import org.udg.pds.todoandroid.TodoApp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CrearPostFragment extends Fragment {

    TodoApi mTodoService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_crear_post, container, false);

        mTodoService = ((TodoApp) getActivity().getApplication()).getAPI();

        final EditText editTextTitle = view.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = view.findViewById(R.id.editTextDescription);
        final EditText editTextPrice = view.findViewById(R.id.editTextPrice);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmitPost);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                double price;
                try {
                    price = Double.parseDouble(editTextPrice.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Preu invalid", Toast.LENGTH_LONG).show();
                    return;
                }

                Call<ResponseBody> call = mTodoService.addPost(new Post(title,description,price));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Post creat correctament", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Error afegint el post", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Fallada afegint el post: ", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        return view;
    }
}
