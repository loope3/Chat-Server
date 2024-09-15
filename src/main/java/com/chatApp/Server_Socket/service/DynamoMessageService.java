package com.chatApp.Server_Socket.service;

import com.chatApp.Server_Socket.model.Message;
import com.chatApp.Server_Socket.repository.DynamoMessageRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DynamoMessageService {

    private final DynamoMessageRepo messageRepository;

    public DynamoMessageService(DynamoMessageRepo messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message) {
        // Generate a unique ID for the message
        if (message.getId() == null) {
            message.setId(java.util.UUID.randomUUID().toString());
        }
        messageRepository.saveMessage(message);
    }

    public Optional<Message> getMessage(String id) {
        return messageRepository.getMessage(id);
    }

    public List<Message> getAllMessages() {
        return messageRepository.getAllMessages();
    }
}
