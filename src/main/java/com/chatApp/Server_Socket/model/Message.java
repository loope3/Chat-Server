package com.chatApp.Server_Socket.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.time.Instant;

@DynamoDbBean
public class Message {
    private String id;
    private User user;
    private String receiverId;
    private String comment;
    private Action action;
    private Instant timestamp;

    public Message() {}

    public Message(User user, String receiverId, String comment, Action action, Instant timestamp) {
        this.user = user;
        this.receiverId = receiverId;
        this.comment = comment;
        this.action = action;
        this.timestamp = timestamp;
    }


    @DynamoDbPartitionKey
    @DynamoDbAttribute("Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbAttribute("User")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @DynamoDbAttribute("ReceiverId")
    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @DynamoDbAttribute("Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @DynamoDbAttribute("Action")
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @DynamoDbAttribute("Timestamp")
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
