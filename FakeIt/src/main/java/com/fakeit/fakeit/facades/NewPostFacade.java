package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;
import com.fakeit.fakeit.services.PostService;
import org.springframework.stereotype.Component;

@Component
public class NewPostFacade {

    private final PostService postService;

    public NewPostFacade(PostService postService) {
        this.postService = postService;
    }

    public PostDto newPost(NewPostDto newPostDto) {
        if (postService.addNewPost(newPostDto)) {
            return postService.getPostById(newPostDto.getPhotoId());
        }else {
            return null;
        }

    }

}
