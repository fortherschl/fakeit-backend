package com.fakeit.fakeit.facades;

import com.fakeit.fakeit.dtos.*;
import com.fakeit.fakeit.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupFacade {

    private final GroupService service;

    public GroupDto create(GroupCreateDto dto, String userId) {
        return service.createGroup(dto, userId);
    }

    public List<GroupDto> groupsOfUser(String userId) {
        return service.groupsOfUser(userId);
    }

    public List<GroupDto> groupsNotIn(String userId) {
        return service.groupsUserIsNotIn(userId);
    }

    public GroupDto get(String id) { return service.getGroup(id); }

    public GroupDetailsDto details(String id, String requester) {
        return service.getGroupDetails(id, requester);
    }

    public GroupDto join(String id, String user) { return service.joinPublicGroup(id, user); }

    public void leave(String id, String user) { service.leaveGroup(id, user); }

    public void invite(String id, String admin, String invited) {
        service.inviteUser(id, admin, invited);
    }

    public void requestJoin(String id, String user) {
        service.requestJoinPrivate(id, user);
    }

    public void remove(String id, String admin, String target) {
        service.removeUser(id, admin, target);
    }

    public void delete(String id, String admin) {
        service.deleteGroup(id, admin);
    }
}
