package com.parstegram.konceq.parstegram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class ProfileFragment extends Fragment {

    private static final String AUTHORITY = "com.parstegram.konceq.parstegram";

    private Button logoutButton;
    private Button profileButton;
    private ImageView imageView;
    private Button postButton;
    Bitmap bitmap;
    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        logoutButton = view.findViewById(R.id.logoutBtn);
        profileButton = view.findViewById(R.id.profileBtn);
        imageView = view.findViewById(R.id.profilePic);
        postButton = view.findViewById(R.id.postBtn);

        final ParseUser user = ParseUser.getCurrentUser();
        System.out.println(user);
        final ParseFile pic = user.getParseFile("profilePic");
        if(pic != null) {
            Glide.with(this).load(pic.getUrl())
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(imageView);
        }

        profileButton.setOnClickListener(new View.OnClickListener() {
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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(ProfileFragment.this.getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser user = ParseUser.getCurrentUser();
                if(file == null){
                    Toast.makeText(ProfileFragment.super.getContext(), "Must include an image in order to post", Toast.LENGTH_LONG).show();
                    return;
                }

                final ParseFile parseFile = new ParseFile(file);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        user.put("profilePic", parseFile);
                        user.saveInBackground();
                        Glide.with(ProfileFragment.this.getContext()).load(pic.getUrl())
                                .apply(bitmapTransform(new CircleCrop()))
                                .into(imageView);
                        Toast.makeText(ProfileFragment.super.getContext(), "Profile pic updated", Toast.LENGTH_LONG).show();
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



}
