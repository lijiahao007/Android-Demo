package com.example.myapplication.unsplashproject.onlyokhttp.entity;

public class UnsplashUrl {
    String raw;
    String full;
    String regular;
    String small;
    String thumb;
    String small_s3;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSmall_s3() {
        return small_s3;
    }

    public void setSmall_s3(String small_s3) {
        this.small_s3 = small_s3;
    }

    @Override
    public String toString() {
        return "UnsplashUrl{" +
                "raw='" + raw + '\'' +
                ", full='" + full + '\'' +
                ", regular='" + regular + '\'' +
                ", small='" + small + '\'' +
                ", thumb='" + thumb + '\'' +
                ", small_s3='" + small_s3 + '\'' +
                '}';
    }
}
