package com.fakeit.fakeit.services;


import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;
import com.fakeit.fakeit.dtos.PostEnhancedDto;

import java.util.List;

public interface PostService {
    boolean addNewPost(NewPostDto newPostDto);
    PostDto getPostById(String id);
    List<PostDto> getPostsByGroup(String groupId);
    List<PostDto> getPostsHidden(String groupId);
    List<PostEnhancedDto> getPostsEnhanced(String groupId);
    PostDto getPostSafe(String id);
}