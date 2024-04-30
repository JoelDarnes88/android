package org.udg.pds.todoandroid.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.udg.pds.todoandroid.R;
import org.udg.pds.todoandroid.databinding.FragmentCrearPostBinding;

import org.udg.pds.todoandroid.rest.TodoApi;
import org.udg.pds.todoandroid.TodoApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CrearPostFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    TodoApi mTodoService;
    private Uri[] selectedImages = new Uri[4];
    private FragmentCrearPostBinding binding;
    private ImageView[] imageViews = new ImageView[4];

    private List<MultipartBody.Part> files = new ArrayList<>();

    public CrearPostFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCrearPostBinding.inflate(inflater);

        setupImageViews();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mTodoService = ((TodoApp) getActivity().getApplication()).getAPI();

        EditText editTextTitle = binding.editTextTitle;
        EditText editTextDescription = binding.editTextDescription;
        EditText editTextPrice = binding.editTextPrice;
        Button buttonSubmit = binding.buttonSubmitPost;

        binding.buttonSubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                double price;
                try {
                    price = Double.parseDouble(editTextPrice.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Preu invalid", Toast.LENGTH_LONG).show();
                    return;
                }


                for (Uri si : selectedImages) { //TRACTAMENT IMATGES
                    if (si != null) {
                        try {
                            InputStream is = getContext().getContentResolver().openInputStream(si);
                            String extension = "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(si));
                            File tempFile = File.createTempFile("upload", extension, getContext().getCacheDir());
                            FileOutputStream outs = new FileOutputStream(tempFile);
                            IOUtils.copy(is, outs);
                            // create RequestBody instance from file
                            RequestBody requestFile =
                                RequestBody.create(
                                    MediaType.parse(getContext().getContentResolver().getType(si)),
                                    tempFile
                                );

                            // MultipartBody.Part is used to send also the actual file name
                            MultipartBody.Part body =
                                MultipartBody.Part.createFormData("files", tempFile.getName(), requestFile);

                            files.add(body);


                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                //RequestBody per cada camp
                RequestBody titol = RequestBody.create(MediaType.parse("multipart/form-data"), title);
                RequestBody descripcio = RequestBody.create(MediaType.parse("multipart/form-data"), description);
                RequestBody preu = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(price));

                //ENDPOINT
                Call<ResponseBody> call = mTodoService.addPostImages(titol, descripcio, preu, files);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Post creat OK!", Toast.LENGTH_SHORT).show();
                            clearFormFields(editTextTitle, editTextDescription, editTextPrice);
                            resetImageSelections();
                        } else
                            Toast.makeText(getContext(), "Response error !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void clearFormFields(EditText editTextTitle, EditText editTextDescription, EditText editTextPrice) {
        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
    }
    private void resetImageSelections() {
        for (int i = 0; i < selectedImages.length; i++) {
            selectedImages[i] = null;
            imageViews[i].setImageResource(R.drawable.add_img);
        }
        files.clear();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            int imageIndex = requestCode - PICK_IMAGE_REQUEST;
            if (imageIndex >= 0 && imageIndex < selectedImages.length) {
                selectedImages[imageIndex] = data.getData();
                imageViews[imageIndex].setImageURI(selectedImages[imageIndex]);
            }
        }
    }

    private void setupImageViews() {
        imageViews[0] = binding.selectedImagePreview1;
        imageViews[1] = binding.selectedImagePreview2;
        imageViews[2] = binding.selectedImagePreview3;
        imageViews[3] = binding.selectedImagePreview4;

        for (int i = 0; i < imageViews.length; i++) {
            int finalI = i;
            imageViews[i].setOnClickListener(v -> pickImage(finalI));
        }
    }
    private void pickImage(int imageIndex) {
        resetSingleImageSelection(imageIndex);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST + imageIndex);
    }
    private void resetSingleImageSelection(int imageIndex) {
        if (selectedImages[imageIndex] != null) {
            selectedImages[imageIndex] = null;
            imageViews[imageIndex].setImageResource(R.drawable.add_img);
        }
    }
}
