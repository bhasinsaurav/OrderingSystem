package com.ren.orderingSystem.Controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Headers;

import java.util.Map;

@Controller
public class TestWebSocketController {

    // Receives messages from destination: /app/test
    @MessageMapping("/test")
    @SendToUser("/queue/test-reply") // This sends the response to /user/queue/test-reply
    public String handleTestMessage(@Payload String message, @Headers Map<String, Object> headers) {
        System.out.println("📥 Received test message from client: " + message);
        return "🧪 Backend received your message: " + message;
    }
}
