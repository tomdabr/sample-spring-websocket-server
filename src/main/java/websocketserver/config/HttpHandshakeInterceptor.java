package websocketserver.config;

import websocketserver.security.JwtTokenVerifier;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@RequiredArgsConstructor
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);

  private final JwtTokenVerifier jwtTokenVerifier;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Map attributes) {
    LOG.info("Received request from the frontend");
    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      String token = servletRequest.getServletRequest().getParameter("token");
      LOG.info("Handled request from the frontend");
      return jwtTokenVerifier.isTokenValid(token);
    }
    LOG.info("Handled request from the frontend");
    return true;
  }

  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
      WebSocketHandler wsHandler, Exception ex) {
    // Do nothing
  }
}
