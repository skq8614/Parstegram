package com.parstegram.konceq.parstegram;

        import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_PROFILEPIC = "profilePic";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseUser getTime(){ return getParseUser(KEY_CREATED_AT);}

    public ParseFile getProfilePic() {
        return getParseUser(KEY_USER).getParseFile(KEY_PROFILEPIC);
    }

    public void setProfilePic(ParseFile image) {put(KEY_PROFILEPIC, image);}

    public static class Query extends ParseQuery<Post>{

        public Query() {
            super(Post.class);
        }

        public Query getTop(){
            setLimit(20);
            orderByDescending("createdAt");
            return this;
        }

        public Query withUser(){
            include("user");
            return this;
        }
    }

}
