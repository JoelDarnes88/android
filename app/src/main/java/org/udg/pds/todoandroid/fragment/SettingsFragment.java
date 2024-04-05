package org.udg.pds.todoandroid.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import org.udg.pds.todoandroid.databinding.ContentSettingsBinding;
import org.udg.pds.todoandroid.activity.LogOut;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private ContentSettingsBinding binding;

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
            String newUsername = binding.editTextChangeUsername.getText().toString();
            String newPassword = binding.editTextChangePassword.getText().toString();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", newUsername);
            editor.putString("password", newPassword);
            editor.apply();

            Toast.makeText(getActivity(), "Cambios guardados", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogout.setOnClickListener(view -> {
            new LogOut().performLogout(getActivity());
        });


        return binding.getRoot();
    }
}
