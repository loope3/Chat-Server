package com.chatApp.Server_Socket.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class User {
    private String id;
    private String serialId;
    private String username;

    public User() {}

    public User(String id, String serialId, String username) {
        this.id = id;
        this.serialId = serialId;
        this.username = username;
    }

    @DynamoDbAttribute("Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbAttribute("SerialId")
    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    @DynamoDbAttribute("Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
