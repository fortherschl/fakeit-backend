package com.fakeit.fakeit.models;

import java.time.LocalDateTime;

public class Vote {
    private LocalDateTime voteDate;
    private String photoId;
    private boolean real;
    private String userId;
    private String voteId;

    public Vote(LocalDateTime voteDate, String photoId, boolean real, String userId, String voteId) {
        this.voteDate = voteDate;
        this.photoId = photoId;
        this.real = real;
        this.userId = userId;
        this.voteId = voteId;
    }
}
