package es.jaimelozanodiegotorres.backapp.config.websocket;

import es.jaimelozanodiegotorres.backapp.interceptors.CheckOriginWsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CheckOriginWsFilter filter;

    @Autowired
    public WebSocketConfig(CheckOriginWsFilter filter) {
        this.filter = filter;
    }

    // wss://localhost:3000/v1/ws/orders
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketOrdersHandler(), "/ws/orders")
                .setAllowedOrigins("http://localhost:4200")
                .setAllowedOrigins("https://bio-online.netlify.app")
                .addInterceptors(filter)
        ;
    }

    // Cada uno de los handlers como bean para que cada vez que nos atienda
    @Bean
    public WebSocketHandler webSocketOrdersHandler() {
        return new WebSocketHandler("Orders");
    }

}
