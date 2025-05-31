package com.fakeit.fakeit.dtos;

import lombok.Data;
import java.util.List;

@Data
public class GroupDetailsDto {
    private GroupDto grupo;
    private List<SimpleUserDto> miembros;
    private boolean esAdmin;
}
