package com.rootsandpots.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public void broadcastTableUpdate(UUID tableId, boolean available) {
        Map<String, Object> update = new HashMap<>();
        update.put("tableId", tableId.toString());
        update.put("available", available);
        update.put("timestamp", System.currentTimeMillis());
        
        messagingTemplate.convertAndSend("/topic/table-updates", update);
    }
}