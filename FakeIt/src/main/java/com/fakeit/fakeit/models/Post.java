package com.fakeit.fakeit.models;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
    private String description;
    private LocalDateTime postDate;
    private String format;
    private String photoId;
    private boolean real;
    private String title;
    private String url;
    private String userId;
    private List<Vote> votes;

    public Post(String description, LocalDateTime postDate, String format, String photoId, boolean real, String title, String url, String userId, List<Vote> votes) {
        this.description = description;
        this.postDate = postDate;
        this.format = format;
        this.photoId = photoId;
        this.real = real;
        this.title = title;
        this.url = url;
        this.userId = userId;
        this.votes = votes;
    }
}
