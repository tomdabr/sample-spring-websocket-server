package websocketserver.security;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

  @Value("${auth.tokenHeaderName}")
  private String principalRequestHeader;

  @Value("${auth.token}")
  private String principalRequestValue;

  @Override
  public void configure(HttpSecurity http) throws Exception {

    APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);

    filter.setAuthenticationManager(authentication -> {
      String principal = (String) authentication.getPrincipal();
      if (!principalRequestValue.equals(principal))
      {
        throw new BadCredentialsException("The API key was not found or not the expected value.");
      }
      authentication.setAuthenticated(true);
      return authentication;
    });

    http
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/actuator/health").permitAll()
        .antMatchers("/actuator/info").permitAll()
        .antMatchers("/actuator/prometheus").permitAll()
        .antMatchers("/sandbox-output-fe/**").permitAll()//token is validated in HttpHandshakeInterceptor
        .and().addFilter(filter).authorizeRequests().anyRequest().authenticated();

  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
