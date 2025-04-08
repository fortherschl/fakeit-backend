package com.fakeit.fakeit.models;

import java.time.LocalDateTime;
import java.util.List;

public class User {
    private boolean admin = false;
    private String email;
    private LocalDateTime joinDate;
    private String profilePicture;
    private List<Group> groups;
    private String username;
    private int eloPoints;
    private UserRank rank;
    private LocalDateTime lastSeenDate;
    private String userId;

    public User(boolean admin,
                String email,
                LocalDateTime joinDate,
                String profilePicture,
                List<Group> groups,
                String username,
                int eloPoints,
                UserRank rank,
                LocalDateTime lastSeenDate,
                String userId) {
        this.admin = admin;
        this.email = email;
        this.joinDate = joinDate;
        this.profilePicture = profilePicture;
        this.groups = groups;
        this.username = username;
        this.eloPoints = eloPoints;
        this.rank = rank;
        this.lastSeenDate = lastSeenDate;
        this.userId = userId;
    }
}
