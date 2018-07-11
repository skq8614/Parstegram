package com.parstegram.konceq.parstegram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    ArrayList<Post> posts;
    RecyclerView feed;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feed = findViewById(R.id.feed);
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);
        feed.setLayoutManager(new LinearLayoutManager(this));
        feed.setAdapter(postAdapter);

        populateTimeline();
    }

    public void populateTimeline(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class).include("user");
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    posts.addAll(itemList);
                    postAdapter.notifyDataSetChanged();
                    Toast.makeText(FeedActivity.this, "Added post", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
}

