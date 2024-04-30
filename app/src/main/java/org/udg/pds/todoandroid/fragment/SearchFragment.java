package org.udg.pds.todoandroid.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.entity.User;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    TodoApi mTodoService;
    List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userList = (List<User>) getArguments().getSerializable("userList");
        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        mTodoService = ((TodoApp) this.getActivity().getApplication()).getAPI();

        ConstraintLayout userContainer = layout.findViewById(R.id.container);

        for (int i = 0; i < userList.size(); i++) {
            View userItemView = inflater.inflate(R.layout.search_profile, userContainer, false);
            TextView nameTV = userItemView.findViewById(R.id.name);
            TextView usernameTV = userItemView.findViewById(R.id.username);

            nameTV.setText(userList.get(i).getName());
            String usernameText = getString(R.string.at_sign) + userList.get(i).getUsername();
            usernameTV.setText(usernameText);

            Button profile = userItemView.findViewById(R.id.profile);
            final User currentUser = userList.get(i);
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putSerializable("foreignUser", (Serializable) currentUser);
                    NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.profileFragment, args);
                }
            });

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userItemView.getLayoutParams();
            float scale = getResources().getDisplayMetrics().density;
            params.topMargin = (int) (i * 100 * scale + 0.5f);
            userItemView.setLayoutParams(params);

            userContainer.addView(userItemView);
        }

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

        return layout;
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
                            NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.searchFragment, args);
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
