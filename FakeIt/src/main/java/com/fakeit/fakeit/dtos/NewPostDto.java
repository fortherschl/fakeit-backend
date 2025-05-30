package com.fakeit.fakeit.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class NewPostDto {
    private String title;
    private String url;
    private boolean real;
    private String userId;
    private String grupoId;
}
