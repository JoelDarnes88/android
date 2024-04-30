package org.udg.pds.todoandroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.activity.MeusPostsActivity;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    public static final String SHARED_PREFS_KEY = "loggedUser";

    TodoApi mTodoService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_profile, container, false);
        mTodoService = ((TodoApp) this.getActivity().getApplication()).getAPI();

        Button search = layout.findViewById(R.id.searchIcon);
        EditText searchBar = layout.findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    handleInput(searchBar);
                    return true;
                }
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleInput(searchBar);
            }
        });

        Button settings = layout.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.settingsFragment); }
        });

        Button openProfileButton = layout.findViewById(R.id.boto_meus_posts);
        openProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MeusPostsActivity.class);
            startActivity(intent);
        });

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameTV = view.findViewById(R.id.name);
        TextView usernameTV = view.findViewById(R.id.username);
        TextView countryTV = view.findViewById(R.id.country);
        TextView aboutMeTV = view.findViewById(R.id.aboutMeDescription);

        String name, username, country, aboutMe;
        Bundle args = getArguments();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        if(args != null && args.containsKey("foreignUser")){
            User foreignUser = (User) args.getSerializable("foreignUser");
            assert foreignUser != null;
            if(!Objects.equals(foreignUser.getUsername(), sharedPreferences.getString("username", ""))){
                Button settings = view.findViewById(R.id.settings);
                settings.setVisibility(View.INVISIBLE);
                Button posts = view.findViewById(R.id.boto_meus_posts);
                posts.setVisibility(View.INVISIBLE);
                settings.setClickable(false);
                name = foreignUser.getName();
                username = foreignUser.getUsername();
                country = foreignUser.getCountry();
                aboutMe = foreignUser.getAboutMe();
            }
            else{
                name = sharedPreferences.getString("name", "");
                username = sharedPreferences.getString("username", "");
                country = sharedPreferences.getString("country", "");
                aboutMe = sharedPreferences.getString("aboutMe", "");
            }
        }
        else{
            name = sharedPreferences.getString("name", "");
            username = sharedPreferences.getString("username", "");
            country = sharedPreferences.getString("country", "");
            aboutMe = sharedPreferences.getString("aboutMe", "");
        }

        if (name != null) nameTV.setText(name);
        if (username != null) {
            String usernameText = getString(R.string.at_sign) + username;
            usernameTV.setText(usernameText);
        }
        if (country != null) countryTV.setText(country);
        if (aboutMe != null && !aboutMe.equals("")) aboutMeTV.setText(aboutMe);
    }

    private void handleInput(EditText searchBar) {
        String input = searchBar.getText().toString();
        if (input.isEmpty()) Toast.makeText(getActivity(), "Empty Search", Toast.LENGTH_SHORT).show();
        else {
            Call<List<User>> call = mTodoService.searchUser(input);
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {
                        List<User> userList = response.body();
                        if (userList != null && !userList.isEmpty()) {
                            Bundle args = new Bundle();
                            args.putSerializable("userList", (Serializable) userList);
                            NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.searchFragment, args);
                        }
                        else Toast.makeText(getActivity(), "No results found", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getActivity(), "Failed to retrieve search results", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
