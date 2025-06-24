package com.ren.orderingSystem.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder message ){

        message.simpDestMatchers("/app/**").authenticated()  // incoming messages
                .simpDestMatchers("/user/**").authenticated()  // user specific subscription
                .simpDestMatchers("/topic/**").permitAll() // broadcast subscription
                .anyMessage().denyAll();

        return message.build();
    }
}
