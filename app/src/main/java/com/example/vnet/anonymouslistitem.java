package com.example.vnet;

public class anonymouslistitem {
    String post;
    String likes,dislikes;
String id;
public String location;
    public anonymouslistitem() {
    }
 public anonymouslistitem(String post, String likes, String dislikes, String id, String location) {
        this.post = post;
        this.likes = likes;
        this.dislikes = dislikes;
        this.id = id;
        this.location = location;
    }

    public String getPost() {
        return this.post;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getLikes() {
        return this.likes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDislikes() {
        return this.dislikes;
    }


    public String getId() {
return this.id;
    }
}
