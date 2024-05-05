package org.udg.pds.todoandroid.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.TodoApp;
import org.udg.pds.todoandroid.entity.Post;
import org.udg.pds.todoandroid.entity.Servei;
import org.udg.pds.todoandroid.rest.TodoApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPostActivity extends AppCompatActivity {

    TodoApi mTodoService;
    private EditText editTextTitle, editTextDescription, editTextPrice;
    private String postId;
    private String nomServei[];
    private ImageView[] imageViews = new ImageView[4];
    private List<String> originalImageUrls = new ArrayList<>();
    private List<String> urlsToDelete = new ArrayList<>();
    private Uri[] newImageUris = new Uri[4];
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_post);

        mTodoService = ((TodoApp) getApplication()).getAPI();

        setupImageViewListeners();

        Spinner serveis = findViewById(R.id.spinnerServei);
        Call<List<Servei>> call = mTodoService.getServices();
        call.enqueue(new Callback<List<Servei>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Servei>> call, Response<List<Servei>> response) {
                if (response.isSuccessful()) {
                    List<Servei> serviceList = response.body();
                    List<String> serviceNames = new ArrayList<>();
                    serviceNames.add("Escollir tipus servei");
                    assert serviceList != null;
                    for (Servei servei : serviceList) {
                        serviceNames.add(servei.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        EditarPostActivity.this,
                        android.R.layout.simple_spinner_item,
                        serviceNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    serveis.setAdapter(adapter);
                } else {
                    Toast.makeText(EditarPostActivity.this, "Failed to retrieve services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Servei>> call, Throwable t) {
                Toast.makeText(EditarPostActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        nomServei = new String[1];
        serveis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                nomServei[0] = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(EditarPostActivity.this, "Cap opció seleccionada", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonSave = findViewById(R.id.buttonSavePost);
        buttonSave.setOnClickListener(v -> savePostChanges(postId));

        postId = getIntent().getStringExtra("POST_ID");
        loadPostData(postId);
    }

    private void setupImageViewListeners() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        imageViews[0] = findViewById(R.id.editImagePreview1);
        imageViews[1] = findViewById(R.id.editImagePreview2);
        imageViews[2] = findViewById(R.id.editImagePreview3);
        imageViews[3] = findViewById(R.id.editImagePreview4);

        for (int i = 0; i < imageViews.length; i++) {
            int finalI = i;
            imageViews[finalI].setOnClickListener(v -> handleImageClick(finalI));
        }
    }

    private void handleImageClick(int index) {
        CharSequence[] options = newImageUris[index] != null || (originalImageUrls.size() > index && originalImageUrls.get(index) != null) ?
            new CharSequence[]{"Reemplaçar Imatge", "Eliminar Imatge"} : new CharSequence[]{"Seleccionar Imatge"};

        new AlertDialog.Builder(this)
            .setTitle("Escollir Opcio")
            .setItems(options, (dialog, which) -> {
                if (which == 0) {
                    if (originalImageUrls.size() > index && originalImageUrls.get(index) != null) {
                        if (!urlsToDelete.contains(originalImageUrls.get(index))) {
                            urlsToDelete.add(originalImageUrls.get(index));
                        }
                    }
                    pickImage(index);
                } else if (which == 1) {
                    removeImage(index);
                }
            })
            .show();
    }

    private void pickImage(int index) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imatge"), index);
    }

    private void removeImage(int index) {
        if (index < originalImageUrls.size() && originalImageUrls.get(index) != null) {
            urlsToDelete.add(originalImageUrls.get(index));
            originalImageUrls.set(index, null);
        }
        newImageUris[index] = null;
        imageViews[index].setImageResource(R.drawable.add_img);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null && requestCode < imageViews.length) {
            newImageUris[requestCode] = data.getData();
            imageViews[requestCode].setImageURI(newImageUris[requestCode]);
        }
    }

    private void loadPostData(String postId) {

        Call<Post> call = mTodoService.getPostId(postId);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post post = response.body();
                    editTextTitle.setText(post.getTitol());
                    editTextDescription.setText(post.getDescripcio());
                    editTextPrice.setText(String.valueOf(post.getPreu()));
                    Spinner spinner = findViewById(R.id.spinnerServei);

                    originalImageUrls = post.getImages();
                    for (int i = 0; i < imageViews.length; i++) {
                        if (i < originalImageUrls.size() && originalImageUrls.get(i) != null) {
                            Picasso.get().load(originalImageUrls.get(i)).into(imageViews[i]);
                        } else {
                            imageViews[i].setImageResource(R.drawable.add_img);
                        }
                    }
                } else {
                    Toast.makeText(EditarPostActivity.this, "Fallada obtenint el post", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(EditarPostActivity.this, "Error carregant el post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) return i;
        }
        return 0;
    }

    private void savePostChanges(String post_id) {
        String title  = editTextTitle.getText().toString();
        String description  = editTextDescription.getText().toString();
        double price = Double.parseDouble(editTextPrice.getText().toString());
        try {
            price = Double.parseDouble(editTextPrice.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Preu invalid", Toast.LENGTH_LONG).show();
            return;
        }
        if (Objects.equals(nomServei[0], "Escollir tipus servei")) Toast.makeText(this, "Servei no escollit", Toast.LENGTH_LONG).show();
        else {
            List<MultipartBody.Part> files = new ArrayList<>();
            for (Uri imageUri : newImageUris) {
                if (imageUri != null) {
                    try {
                        File file = createImageFileFromUri(imageUri);
                        RequestBody requestFile = RequestBody.create(
                            MediaType.parse(getContentResolver().getType(imageUri)),
                            file
                        );
                        MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                        files.add(body);
                    } catch (Exception e) {
                        Toast.makeText(this, "Error preparant imatge: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            RequestBody titol = RequestBody.create(MediaType.parse("multipart/form-data"), title);
            RequestBody descripcio = RequestBody.create(MediaType.parse("multipart/form-data"), description);
            RequestBody preu = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(price));
            RequestBody servei = RequestBody.create(MediaType.parse("multipart/form-data"), nomServei[0]);

            Call<ResponseBody> call = mTodoService.updatePostImage(postId, titol, descripcio, preu, servei, urlsToDelete, files);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditarPostActivity.this, "Post actualitzat OK!!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditarPostActivity.this, "Error actualitzant el post", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(EditarPostActivity.this, "Error de xarxa", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private File createImageFileFromUri(Uri uri) throws Exception {
        InputStream is = getContentResolver().openInputStream(uri);
        String mimeType = getContentResolver().getType(uri);
        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        File tempFile = File.createTempFile("upload_", "." + extension, getCacheDir());
        FileOutputStream out = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        is.close();
        out.close();
        return tempFile;
    }
}
