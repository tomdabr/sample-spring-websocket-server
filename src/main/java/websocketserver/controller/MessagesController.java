package websocketserver.controller;

import websocketserver.dto.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessagesController {

  private static final Logger LOG = LoggerFactory.getLogger(MessagesController.class);

  private final SimpMessagingTemplate simpMessagingTemplate;
  private final ObjectMapper objectMapper;

  @MessageMapping("/output")
  public void handleOutput(@Payload String message) throws JsonProcessingException {
    LOG.debug("Received message: {}", message);

    LogMessage logMessage = objectMapper.readValue(message, LogMessage.class);
    this.simpMessagingTemplate.convertAndSend(
        "/queue/output-" + logMessage.getFlowRunId(), message);
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception) {
    return exception.getMessage();
  }
}
