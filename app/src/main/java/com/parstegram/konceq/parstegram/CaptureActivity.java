package com.parstegram.konceq.parstegram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CaptureActivity extends AppCompatActivity {

    private static final String AUTHORITY = "com.parstegram.konceq.parstegram";

    private EditText descriptionInput;
    private Button createButton;
    private Button logoutButton;
    private ImageView imageView;
    private Button captureButton;
    private Button feedBtn;
    Bitmap bitmap;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        descriptionInput = findViewById(R.id.description_et);
        createButton = findViewById(R.id.create_btn);
        logoutButton = findViewById(R.id.logoutBtn);
        imageView = findViewById(R.id.photo2);
        feedBtn = findViewById(R.id.feedBtn);
        captureButton = findViewById(R.id.captureBtn);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
                    Toast.makeText(CaptureActivity.super.getBaseContext(), "Must include an image and caption in order to post", Toast.LENGTH_LONG).show();
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(CaptureActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CaptureActivity.this, FeedActivity.class);
                startActivity(i);
            }
        });

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Uri uri = FileProvider.getUriForFile(this, AUTHORITY, file);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Toast.makeText(this, "Added post", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(CaptureActivity.this, FeedActivity.class);
        startActivity(i);
    }


    private void loadTopPosts(){
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e==null){
                    for(int i = 0; i < objects.size(); ++i) {
                        Log.d("CaptureActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }
}
