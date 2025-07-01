package com.ren.orderingSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier; // *** NEW IMPORT: Need to import Supplier ***

@Configuration
@EnableWebSocketSecurity
@Slf4j
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder message) {

        log.info("Configuring STOMP message authorization manager.");

        AuthorizationManager<Message<?>> authManager = message
                .simpTypeMatchers(SimpMessageType.CONNECT).permitAll()
                .simpTypeMatchers(SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT).permitAll()
                .simpDestMatchers("/app/**").authenticated()
                .simpDestMatchers("/user/**").authenticated()
                .simpDestMatchers("/topic/**").permitAll()
                .anyMessage().authenticated()
                .build();

        // The 'authentication' parameter here is actually a Supplier<Authentication>
        return (authenticationSupplier, msg) -> { // Renamed parameter for clarity
            StompHeaderAccessor accessor = MessageHeaderAccessor
                    .getAccessor(msg, StompHeaderAccessor.class);
            String destination = accessor.getDestination();
            SimpMessageType messageType = accessor.getMessageType();

            log.info("Authorization check initiated for [Type: {}, Destination: {}]",
                    messageType, destination);

            // *** FIX: Get the actual Authentication object from the Supplier ***
            Authentication authentication = authenticationSupplier.get();

            if (authentication != null) {
                log.info("Authentication object present. Principal: {}, Authenticated: {}, Type: {}",
                        authentication.getPrincipal(), authentication.isAuthenticated(), authentication.getClass().getSimpleName());
                if (authentication instanceof AnonymousAuthenticationToken) {
                    log.warn("Authentication is AnonymousAuthenticationToken. This means no user was explicitly authenticated yet.");
                }
            } else {
                log.warn("Authentication object is NULL during authorization check.");
            }

            // Perform the actual authorization decision
            AuthorizationDecision decision = authManager.check(authenticationSupplier, msg); // Pass the Supplier here

            log.info("Authorization check completed for [Type: {}, Destination: {}]. Decision: {}. Is Granted: {}",
                    messageType, destination, decision, decision != null ? decision.isGranted() : "N/A");

            return decision;
        };
    }

    @Bean
    public boolean sameOriginDisabled() {
        return true;
    }


}
