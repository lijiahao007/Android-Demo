package com.example.myapplication.recycleview.entity;

import java.util.List;

public class UnsplashPhoto {
    String id;
    String created_at;
    String updated_at;
    String promoted_at;
    int width;
    int height;
    String color;
    String blur_hash;
    String description;
    UnsplashUrl urls;
    List<String> categories;
    int likes;
    boolean liked_by_user;
    int views;
    int downloads;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPromoted_at() {
        return promoted_at;
    }

    public void setPromoted_at(String promoted_at) {
        this.promoted_at = promoted_at;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBlur_hash() {
        return blur_hash;
    }

    public void setBlur_hash(String blur_hash) {
        this.blur_hash = blur_hash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnsplashUrl getUrls() {
        return urls;
    }

    public void setUrls(UnsplashUrl urls) {
        this.urls = urls;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked_by_user() {
        return liked_by_user;
    }

    public void setLiked_by_user(boolean liked_by_user) {
        this.liked_by_user = liked_by_user;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return "UnsplashPhoto{" +
                "id='" + id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", promoted_at='" + promoted_at + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", color='" + color + '\'' +
                ", blur_hash='" + blur_hash + '\'' +
                ", description='" + description + '\'' +
                ", urls=" + urls +
                ", categories=" + categories +
                ", likes=" + likes +
                ", liked_by_user=" + liked_by_user +
                ", views=" + views +
                ", downloads=" + downloads +
                '}';
    }
}
