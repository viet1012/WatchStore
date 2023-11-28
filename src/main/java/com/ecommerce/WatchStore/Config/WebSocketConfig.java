package com.ecommerce.WatchStore.Config;

import com.ecommerce.WatchStore.WebSocket.MyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(myHandler(), "/myhandler");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }

}

