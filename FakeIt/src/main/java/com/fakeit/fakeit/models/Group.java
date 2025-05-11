package com.fakeit.fakeit.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class Group {
    private List<User> administrators;
    private int userCount;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime lastMessageTime;
    private String groupPic;
    private String groupId;
    private int maxUsers;
    private String groupName;
    private boolean publicGroup;
    private int currentRound;
    private List<User> users;

    public Group(List<User> administrators, int userCount, String description, LocalDateTime creationDate, LocalDateTime lastMessageTime, String groupPic, String groupId, int maxUsers, String groupName, boolean publicGroup, int currentRound, List<User> users) {
        this.administrators = administrators;
        this.userCount = userCount;
        this.description = description;
        this.creationDate = creationDate;
        this.lastMessageTime = lastMessageTime;
        this.groupPic = groupPic;
        this.groupId = groupId;
        this.maxUsers = maxUsers;
        this.groupName = groupName;
        this.publicGroup = publicGroup;
        this.currentRound = currentRound;
        this.users = users;
    }
}
