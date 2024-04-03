package org.udg.pds.todoandroid.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.entity.User;

public class ProfileActivity extends AppCompatActivity {
    public static final String SHARED_PREFS_KEY = "user_data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView nameTV = findViewById(R.id.name);
        TextView usernameTV = findViewById(R.id.username);
        TextView countryTV = findViewById(R.id.country);
        TextView aboutMeTV = findViewById(R.id.aboutMeDescription);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String username = sharedPreferences.getString("username", "");
        String country = sharedPreferences.getString("country", "");
        String aboutMe = sharedPreferences.getString("aboutMe", "");

        if(!name.isEmpty()) nameTV.setText(name);
        if(!username.isEmpty()){
            String usernameText = getString(R.string.at_sign) + username;
            usernameTV.setText(usernameText);
        }
        if(!country.isEmpty()) countryTV.setText(country);
        if(!aboutMe.isEmpty()) aboutMeTV.setText(aboutMe);
    }
}
