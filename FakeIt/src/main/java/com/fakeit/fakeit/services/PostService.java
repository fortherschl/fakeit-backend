package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;

public interface PostService {
    boolean addNewPost(NewPostDto newPost);

    PostDto getPostById(String id);
}
