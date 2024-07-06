package es.jaimelozanodiegotorres.backapp.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Checkea el origen de las consultas al ws solo y solo si el perfil tiene prod
 */
@Component
@Slf4j
public class CheckOriginWsFilter  implements HandshakeInterceptor {
    @Value("${spring.profiles.active}")
    String profile;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(profile.contains("prod")) {
            String url = profile.equals("prod-server") ? "https://bio-online.netlify.app" : "http://localhost:4200";
            String origin = request.getHeaders().getOrigin();
            if (url.equals(origin)) {
                log.info("Encabezado para la conexion al ws Correcta");
                return true;
            }
            response.setStatusCode(HttpStatus.FORBIDDEN);
            log.error("Conexion al ws Incorrecta: no se puede acceder desde el origen: {}", origin);
            return false;
        } else{
            log.info("Conexion al ws Correcta");
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}