package com.fakeit.fakeit.services;

import com.fakeit.fakeit.dtos.*;

import java.util.List;

public interface GroupService {

    GroupDto createGroup(GroupCreateDto dto, String creatorId);

    List<GroupDto> groupsOfUser(String userId);

    List<GroupDto> groupsUserIsNotIn(String userId);

    GroupDto getGroup(String grupoId);

    GroupDetailsDto getGroupDetails(String grupoId, String requesterId);

    GroupDto joinPublicGroup(String grupoId, String userId);

    void leaveGroup(String grupoId, String userId);

    void inviteUser(String grupoId, String adminId, String invitedUserId);

    void requestJoinPrivate(String grupoId, String requesterId);

    void removeUser(String grupoId, String adminId, String usuarioId);

    void deleteGroup(String grupoId, String adminId);
}
