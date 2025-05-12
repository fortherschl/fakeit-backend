package com.fakeit.fakeit.dtos;

import com.fakeit.fakeit.models.Vote;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {
    private LocalDateTime postDate;
    private boolean real;
    private String title;
    private String url;
    private List<Vote> votes;
}
