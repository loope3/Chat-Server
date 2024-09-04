package com.chatApp.Server_Socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class config implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //endpoint to send messages in message broker context
        config.enableSimpleBroker("/topic", "/user");
        //endpoint to send messages in application context
        config.setApplicationDestinationPrefixes("/app");
        //for private messages
        config.setUserDestinationPrefix("/user");
    }

//    @Override
//    public boolean configureMessageConverters(List<MessageConverter> list) {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        list.add(converter);
//        return true;
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //first server connection point
        registry.addEndpoint("/chatApp").setAllowedOrigins("http://localhost:3000");
    }
}
