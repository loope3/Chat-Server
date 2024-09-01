package com.chatApp.Server_Socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Message {
    private final User user;
    private final String receiverId;
    private final String comment;
    private final Action action;
    private final Instant timestamp;
}
