package com.fakeit.fakeit.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostDto {

    private String format;
    private String photoId;
    private boolean real;
    private String title;
    private String url;
    private String userId;

    public NewPostDto(String format, String photoId, boolean real, String title, String url, String userId) {
        this.format = format;
        this.photoId = photoId;
        this.real = real;
        this.title = title;
        this.url = url;
        this.userId = userId;
    }

}
