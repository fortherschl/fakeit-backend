package com.fakeit.fakeit.controllers;

import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;
import com.fakeit.fakeit.facades.NewPostFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class PostController {

    private final NewPostFacade newPostFacade;

    public PostController(NewPostFacade newPostFacade) {
        this.newPostFacade = newPostFacade;
    }
    @PostMapping("/new-post")
    public ResponseEntity<PostDto> createPost(@RequestBody NewPostDto newPostDto) {
        return ResponseEntity.ok(newPostFacade.newPost(newPostDto));
    }
}
