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

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    private final MemberStore memberStore;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(MemberStore memberStore, SimpMessagingTemplate simpMessagingTemplate) {
        this.memberStore = memberStore;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/user")
    public void getusers(User user, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        User newUser = new User(user.getId(), null, user.getUsername());
        headerAccessor.getSessionAttributes().put("user", newUser);
        memberStore.addMember(newUser);
        sendMembersList();
        Message newMessage = new Message(new User(null, null, user.getUsername()), null, null, Action.JOINED, Instant.now());
        simpMessagingTemplate.convertAndSend("/topic/messages", newMessage);

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
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
    }

    @MessageMapping("/message")
    public void sendMessage(Message message)  {
        Message newMessage = new Message(new User(null, message.getUser().getSerialId(), message.getUser().getUsername()), message.getReceiverId(), message.getComment(), message.getAction(), Instant.now());
        simpMessagingTemplate.convertAndSend("/topic/messages", newMessage);
    }

    @MessageMapping("/privateMessage")
    public void getPrivateMessage(Message message) {
        Message newMessage = new Message(new User(null, message.getUser().getSerialId(), message.getUser().getUsername()), message.getReceiverId(), message.getComment(), message.getAction(), Instant.now());
        simpMessagingTemplate.convertAndSendToUser(memberStore.getMembers(message.getReceiverId()).getId(), "/topic/privateMessages", newMessage);
    }

    private void sendMembersList() {
        List<User> memberList = memberStore.getMembersList();
        memberList.forEach(
                sendUser -> simpMessagingTemplate.convertAndSendToUser(sendUser.getId(), "/topic/users/", memberStore.filterMemberListByUser(memberList, sendUser))
        );
    }

}
