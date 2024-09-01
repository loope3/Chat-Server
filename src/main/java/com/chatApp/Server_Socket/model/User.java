package com.chatApp.Server_Socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private final String id;
    private final String username;
}
