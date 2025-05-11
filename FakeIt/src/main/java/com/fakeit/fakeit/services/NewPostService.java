package com.fakeit.fakeit.services;

public interface NewPostService {
    boolean addNewPost(String description, String title, String imageUrl, boolean real, String userId);
}
