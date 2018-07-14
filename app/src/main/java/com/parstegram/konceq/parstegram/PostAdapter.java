package com.parstegram.konceq.parstegram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.List;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    List<Post> mPosts;
    Context context;

    public PostAdapter(List<Post> posts){this.mPosts = posts;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Post post = mPosts.get(i);

        viewHolder.caption.setText(post.getDescription());
        viewHolder.username.setText(post.getUser().getUsername());
        String myTime = TimeFormatter.getTimeDifference(post.getCreatedAt().toString());
        viewHolder.time.setText(myTime);
        if(post.getProfilePic() != null) {
            Glide.with(context).load(post.getProfilePic().getUrl())
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(viewHolder.icon);
        }
        if(post.getImage() != null) {
            Glide.with(context).load(post.getImage().getUrl())
                    .into(viewHolder.photo);
        }

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView photo;
        public TextView username;
        public TextView caption;
        public TextView time;
        public ImageView icon;

        public ViewHolder(View itemView){
            super(itemView);
            photo = itemView.findViewById(R.id.photo2);
            username = itemView.findViewById(R.id.username);
            caption = itemView.findViewById(R.id.caption);
            time = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.myIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Post post = mPosts.get(position);
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("username", post.getUser().getUsername());
                i.putExtra("photo", post.getImage().getUrl());
                i.putExtra("caption", post.getDescription());
                String myTime = TimeFormatter.getTimeDifference(post.getCreatedAt().toString());
                i.putExtra("time", myTime);
                if(post.getProfilePic() != null) {
                    i.putExtra("icon", post.getProfilePic().getUrl());
                }
                else{
                    i.putExtra("icon", "");
                }
                context.startActivity(i);
            }
        }
    }


}
