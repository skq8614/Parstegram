package com.parstegram.konceq.parstegram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView caption;
    private TextView username;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        photo = findViewById(R.id.photo);
        caption = findViewById(R.id.caption);
        username = findViewById(R.id.username);
        time = findViewById(R.id.time);

        String myCaption = getIntent().getStringExtra("caption");
        caption.setText(myCaption);
        String myName = getIntent().getStringExtra("username");
        username.setText(myName);
        String myUrl = getIntent().getStringExtra("photo");
        Glide.with(this).load(myUrl)
                .into(photo);
        String myTime = getIntent().getStringExtra("time");
        time.setText(myTime);
    }
}
