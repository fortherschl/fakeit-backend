package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.*;
import com.fakeit.fakeit.services.PostService;
import org.springframework.stereotype.Component;

import com.fakeit.fakeit.dtos.NewPostDto;
import com.fakeit.fakeit.dtos.PostDto;
import com.fakeit.fakeit.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;

    public boolean createPost(NewPostDto dto) {
        return postService.addNewPost(dto);
    }

    public PostDto getById(String id) {
        return postService.getPostById(id);
    }

    public List<PostDto> getByGroup(String groupId) {
        return postService.getPostsByGroup(groupId);
    }
    public PostDto safeById(String id)          { return postService.getPostSafe(id); }
    public List<PostDto> byGroup(String gid)    { return postService.getPostsByGroup(gid); }
    public List<PostDto> hidden(String gid)     { return postService.getPostsHidden(gid); }
    public List<PostEnhancedDto> enhanced(String gid){ return postService.getPostsEnhanced(gid); }
}
