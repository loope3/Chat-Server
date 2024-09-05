package com.chatApp.Server_Socket.service;

import com.chatApp.Server_Socket.model.User;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MemberStore {
    private static List<User> store = new LinkedList<>();

    public List<User> getMembersList() {
        AtomicInteger serialId = new AtomicInteger();
        return store.stream()
                .map(user -> new User(user.getId(), serialId.getAndIncrement() + "", user.getUsername()))
                .toList();
    }

    public List<User> filterMemberListByUser(List<User> memberList, User user) {
        return memberList.stream()
                .filter(filterUser -> filterUser.getId() != user.getId())
                .map(sendUser -> new User(sendUser.getId(), sendUser.getSerialId(), sendUser.getUsername()))
                .toList();
    }

    public User getMembers(String id) {
        return store.get(Integer.valueOf(id) - 1);
    }

    public void addMember(User member) {
        store.add(member);
    }

    public void removeMember(User member) {
        store.remove(member);
    }
}
