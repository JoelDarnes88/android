package org.udg.pds.todoandroid.fragment;

import static org.udg.pds.todoandroid.activity.Login.SHARED_PREFS_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.databinding.ContentSettingsBinding;
import org.udg.pds.todoandroid.activity.LogOut;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.entity.UserModify;
import org.udg.pds.todoandroid.rest.TodoApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private ContentSettingsBinding binding;
    TodoApi mTodoService;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ContentSettingsBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        binding.switchDarkMode.setChecked(isDarkMode);

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        binding.btnSaveChanges.setOnClickListener(v -> {
            mTodoService = ((TodoApp) getActivity().getApplication()).getAPI();
            String newUsername = binding.editTextChangeUsername.getText().toString();
            String newName = binding.editTextChangeName.getText().toString();
            String newCountry = binding.editTextChangeCountry.getText().toString();
            String newEmail = binding.editTextChangeEmail.getText().toString();
            String newPhoneNumber = binding.editTextChangePhoneNumber.getText().toString();
            String newPassword = binding.editTextChangePassword.getText().toString();
            String newAboutMe = binding.editTextChangeAboutMe.getText().toString();

            Call<User> call = mTodoService.modify(new UserModify(newUsername,newName,newCountry,newEmail,newPhoneNumber,newPassword,newAboutMe));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Dades personals actualitzades", Toast.LENGTH_LONG).show();
                        binding.editTextChangeUsername.setText("");
                        binding.editTextChangePassword.setText("");
                    } else {
                        Toast.makeText(getContext(), "Error modificant les dades personals", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getContext(), "Fallada modificant les dades personals: " + t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", newUsername);
            editor.putString("password", newPassword);
            editor.apply();

            Toast.makeText(getActivity(), "Cambios guardados", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogout.setOnClickListener(view -> {
            new LogOut().performLogout(getActivity());
        });

        binding.btnDeleteAccount.setOnClickListener(view -> {
            mTodoService = ((TodoApp) getActivity().getApplication()).getAPI();
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            String id = sharedPreferences.getString("id", "");
            Call<Void> call = mTodoService.deleteAccount(id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Compta esborrada correctament", Toast.LENGTH_LONG).show();
                        new LogOut().performLogout(getActivity());
                    } else {
                        Toast.makeText(getContext(), "Error esborrant la compta", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Fallada esborrant la compta: " + t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        });

        return binding.getRoot();
    }
}
