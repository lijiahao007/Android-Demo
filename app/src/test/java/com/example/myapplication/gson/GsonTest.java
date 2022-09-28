package com.example.myapplication.gson;

import com.example.myapplication.unsplashproject.onlyokhttp.entity.UnsplashPhoto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

public class GsonTest {

    @Test
    public void test1() {
        String str = "{\n" +
                "  \"id\": \"Dwu85P9SOIk\",\n" +
                "  \"created_at\": \"2016-05-03T11:00:28-04:00\",\n" +
                "  \"updated_at\": \"2016-07-10T11:00:01-05:00\",\n" +
                "  \"width\": 2448,\n" +
                "  \"height\": 3264,\n" +
                "  \"color\": \"#6E633A\",\n" +
                "  \"blur_hash\": \"LFC$yHwc8^$yIAS$%M%00KxukYIp\",\n" +
                "  \"downloads\": 1345,\n" +
                "  \"likes\": 24,\n" +
                "  \"liked_by_user\": false,\n" +
                "  \"description\": \"A man drinking a coffee.\",\n" +
                "  \"exif\": {\n" +
                "    \"make\": \"Canon\",\n" +
                "    \"model\": \"Canon EOS 40D\",\n" +
                "    \"exposure_time\": \"0.011111111111111112\",\n" +
                "    \"aperture\": \"4.970854\",\n" +
                "    \"focal_length\": \"37\",\n" +
                "    \"iso\": 100\n" +
                "  },\n" +
                "  \"location\": {\n" +
                "    \"name\": \"Montreal, Canada\",\n" +
                "    \"city\": \"Montreal\",\n" +
                "    \"country\": \"Canada\",\n" +
                "    \"position\": {\n" +
                "      \"latitude\": 45.473298,\n" +
                "      \"longitude\": -73.638488\n" +
                "    }\n" +
                "  },\n" +
                "  \"current_user_collections\": [\n" +
                "    {\n" +
                "      \"id\": 206,\n" +
                "      \"title\": \"Makers: Cat and Ben\",\n" +
                "      \"published_at\": \"2016-01-12T18:16:09-05:00\",\n" +
                "      \"last_collected_at\": \"2016-06-02T13:10:03-04:00\",\n" +
                "      \"updated_at\": \"2016-07-10T11:00:01-05:00\",\n" +
                "      \"cover_photo\": null,\n" +
                "      \"user\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"urls\": {\n" +
                "    \"raw\": \"https://images.unsplash.com/photo-1417325384643-aac51acc9e5d\",\n" +
                "    \"full\": \"https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg\",\n" +
                "    \"regular\": \"https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=1080&fit=max\",\n" +
                "    \"small\": \"https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=400&fit=max\",\n" +
                "    \"thumb\": \"https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=200&fit=max\"\n" +
                "  },\n" +
                "  \"links\": {\n" +
                "    \"self\": \"https://api.unsplash.com/photos/Dwu85P9SOIk\",\n" +
                "    \"html\": \"https://unsplash.com/photos/Dwu85P9SOIk\",\n" +
                "    \"download\": \"https://unsplash.com/photos/Dwu85P9SOIk/download\",\n" +
                "    \"download_location\": \"https://api.unsplash.com/photos/Dwu85P9SOIk/download\"\n" +
                "  },\n" +
                "  \"user\": {\n" +
                "    \"id\": \"QPxL2MGqfrw\",\n" +
                "    \"updated_at\": \"2016-07-10T11:00:01-05:00\",\n" +
                "    \"username\": \"exampleuser\",\n" +
                "    \"name\": \"Joe Example\",\n" +
                "    \"portfolio_url\": \"https://example.com/\",\n" +
                "    \"bio\": \"Just an everyday Joe\",\n" +
                "    \"location\": \"Montreal\",\n" +
                "    \"total_likes\": 5,\n" +
                "    \"total_photos\": 10,\n" +
                "    \"total_collections\": 13,\n" +
                "    \"instagram_username\": \"instantgrammer\",\n" +
                "    \"twitter_username\": \"crew\",\n" +
                "    \"links\": {\n" +
                "      \"self\": \"https://api.unsplash.com/users/exampleuser\",\n" +
                "      \"html\": \"https://unsplash.com/exampleuser\",\n" +
                "      \"photos\": \"https://api.unsplash.com/users/exampleuser/photos\",\n" +
                "      \"likes\": \"https://api.unsplash.com/users/exampleuser/likes\",\n" +
                "      \"portfolio\": \"https://api.unsplash.com/users/exampleuser/portfolio\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-mm-dd\\T")
                .create();
        UnsplashPhoto unsplashPhoto = gson.fromJson(str, UnsplashPhoto.class);
        System.out.println(unsplashPhoto);
    }
}
