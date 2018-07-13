package com.parstegram.konceq.parstegram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class CaptureFragment extends Fragment {

    private static final String AUTHORITY = "com.parstegram.konceq.parstegram";

    private EditText descriptionInput;
    private Button createButton;
    private ImageView imageView;
    private Button captureButton;
    Bitmap bitmap;
    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        descriptionInput = view.findViewById(R.id.description_et);
        createButton = view.findViewById(R.id.create_btn);
        imageView = view.findViewById(R.id.photo2);
        captureButton = view.findViewById(R.id.captureBtn);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File directory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    file = File.createTempFile("photo", ".jpg", directory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dispatchTakePictureIntent();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                if(file == null || description == null){
                    Toast.makeText(CaptureFragment.super.getContext(), "Must include an image and caption in order to post", Toast.LENGTH_LONG).show();
                    return;
                }

                final ParseFile parseFile = new ParseFile(file);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        createPost(description, parseFile, user);
                    }
                });
            }

        });

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Uri uri = FileProvider.getUriForFile(this.getContext(), AUTHORITY, file);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(this.bitmap);
        }
    }


    public void createPost(String description, ParseFile imageFile, ParseUser user){
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Log.d("CaptureActivity", "Create post success");
                }
                else{
                    e.printStackTrace();
                }
            }
        });
        Toast.makeText(this.getContext(), "Added post", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(this.getContext(), FeedFragment.class);
        //startActivity(i);
        final Fragment fragmentFeed = new FeedFragment();
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragmentFeed).commit();
    }

}
