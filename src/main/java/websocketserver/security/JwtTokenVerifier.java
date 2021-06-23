package websocketserver.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class JwtTokenVerifier {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenVerifier.class);

  @Value("${verifyTokenUrl}")
  private String verifyTokenUrl;

  private final RestTemplate restTemplate;

  public boolean isTokenValid(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    HttpEntity<?> entity = new HttpEntity<>(headers);
    logger.debug("Checking if token is valid...");
    ResponseEntity<String> response = restTemplate.exchange(
        verifyTokenUrl, HttpMethod.GET, entity, String.class);
    logger.debug("response: {}", response.getBody());
    return response.getStatusCode() == HttpStatus.OK;
  }
}
