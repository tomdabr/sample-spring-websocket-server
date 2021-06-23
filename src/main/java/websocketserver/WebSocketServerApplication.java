package websocketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EnableAsync
public class WebSocketServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSocketServerApplication.class, args);
	}
}
