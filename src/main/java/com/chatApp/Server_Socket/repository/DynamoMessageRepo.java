package com.chatApp.Server_Socket.repository;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import java.util.*;
import java.util.stream.Collectors;
import com.chatApp.Server_Socket.model.Message;

@Repository
public class DynamoMessageRepo {
    static final TableSchema<Message> messageTableSchema = TableSchema.fromBean(Message.class);
    private final DynamoDbTable<Message> messageTable;

    public DynamoMessageRepo(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.messageTable = dynamoDbEnhancedClient.table("MessageHistory", TableSchema.fromBean(Message.class));
    }

    public void saveMessage(Message message) {
        messageTable.putItem(message);
    }

    public Optional<Message> getMessage(String id) {
        Message message = messageTable.getItem(Key.builder().partitionValue(id).build());
        return Optional.ofNullable(message);
    }

    public List<Message> getAllMessages() {
        return messageTable.scan()
                .items()
                .stream()
                .collect(Collectors.toList());
    }
}
