package com.chatApp.Server_Socket.chatController;

import com.chatApp.Server_Socket.service.MemberStore;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chatApp.Server_Socket.model.Action;
import com.chatApp.Server_Socket.model.Message;
import com.chatApp.Server_Socket.model.User;
import com.chatApp.Server_Socket.repository.DynamoMessageRepo;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    private final MemberStore memberStore;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DynamoMessageRepo dynamoMessageRepo;
    
    public ChatController(MemberStore memberStore, SimpMessagingTemplate simpMessagingTemplate, DynamoMessageRepo dynamoMessageRepo) {
        this.memberStore = memberStore;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.dynamoMessageRepo = dynamoMessageRepo;
    }

    @MessageMapping("/user")
    public void getusers(User user, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        User newUser = new User(user.getId(), null, user.getUsername());
        headerAccessor.getSessionAttributes().put("user", newUser);
        memberStore.addMember(newUser);
        sendMembersList();

        List<Message> messageHistory = dynamoMessageRepo.getAllMessages();
        messageHistory.forEach(message -> simpMessagingTemplate.convertAndSendToUser(newUser.getId(), "/topic/messages", message));

        Message newMessage = new Message(new User(null, null, user.getUsername()), null, null, Action.JOINED, Instant.now());

        newMessage.setId(java.util.UUID.randomUUID().toString());
        simpMessagingTemplate.convertAndSend("/topic/messages", newMessage);

        dynamoMessageRepo.saveMessage(newMessage);
    }

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
        System.out.println("Connected to session");
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        System.out.println("Disconnected from session");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes == null) {
            return;
        }
        User user = (User) sessionAttributes.get("user");
        if (user == null) {
            return;
        }
        memberStore.removeMember(user);
        sendMembersList();

        Message message = new Message(new User(null, null, user.getUsername()), null, "", Action.LEFT, Instant.now());
        message.setId(java.util.UUID.randomUUID().toString());
        simpMessagingTemplate.convertAndSend("/topic/messages", message);

        dynamoMessageRepo.saveMessage(message);
    }

    @MessageMapping("/message")
    public void sendMessage(Message message) {
        Message newMessage = new Message(
            new User(null, message.getUser().getSerialId(), message.getUser().getUsername()),
            message.getReceiverId(),
            message.getComment(),
            message.getAction(),
            Instant.now()
        );

        newMessage.setId(java.util.UUID.randomUUID().toString());
        simpMessagingTemplate.convertAndSend("/topic/messages", newMessage);

        dynamoMessageRepo.saveMessage(message);
    }

    @MessageMapping("/privateMessage")
    public void getPrivateMessage(Message message) {
        Message newMessage = new Message(
            new User(null, message.getUser().getSerialId(), message.getUser().getUsername()),
            message.getReceiverId(),
            message.getComment(),
            message.getAction(),
            Instant.now()
        );

        newMessage.setId(java.util.UUID.randomUUID().toString());
        simpMessagingTemplate.convertAndSendToUser(
            memberStore.getMembers(message.getReceiverId()).getId(),
            "/topic/privateMessages",
            newMessage
        );

        dynamoMessageRepo.saveMessage(message);
    }

    private void sendMembersList() {
        List<User> memberList = memberStore.getMembersList();
        memberList.forEach(
            sendUser -> simpMessagingTemplate.convertAndSendToUser(
                sendUser.getId(),
                "/topic/users/",
                memberStore.filterMemberListByUser(memberList, sendUser)
            )
        );
    }
}
