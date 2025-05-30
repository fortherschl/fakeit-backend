package com.fakeit.fakeit.controllers;


import com.fakeit.fakeit.dtos.*;
import com.fakeit.fakeit.facades.PostFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostFacade postFacade;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody NewPostDto dto) {
        boolean created = postFacade.createPost(dto);
        return created ? ResponseEntity.ok("Post creado") : ResponseEntity.status(500).body("Error al crear");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable String id) {
        PostDto post = postFacade.getById(id);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<PostDto>> getPostsByGroup(@PathVariable String groupId) {
        return ResponseEntity.ok(postFacade.getByGroup(groupId));
    }
    @GetMapping("/{id}/safe")
    public ResponseEntity<PostDto> getSafe(@PathVariable String id) {
        PostDto p = postFacade.safeById(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @GetMapping("/group/{groupId}/hidden")
    public ResponseEntity<List<PostDto>> listHidden(@PathVariable String groupId) {
        return ResponseEntity.ok(postFacade.hidden(groupId));
    }

    @GetMapping("/group/{groupId}/enhanced")
    public ResponseEntity<List<PostEnhancedDto>> listEnhanced(@PathVariable String groupId) {
        return ResponseEntity.ok(postFacade.enhanced(groupId));
    }
}
