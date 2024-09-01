package com.chatApp.Server_Socket.service;

import com.chatApp.Server_Socket.model.User;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MemberStore {
    private static List<User> store = new LinkedList<>();

    public List<User> getMembers() {
        return store;
    }

    public void addMember(User member) {
        store.add(member);
    }

    public void removeMember(User member) {
        store.remove(member);
    }
}
