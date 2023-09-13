package util;

import com.sum.dto.CallHistoryResponseDTO;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class CallHistoryResponseDTOUtils {

  public static CallHistoryResponseDTO createDefault() {
    return CallHistoryResponseDTO.builder()
        .methodName("methodName")
        .requestMethod("GET")
        .requestPath("/sum")
        .responseStatus(HttpStatus.OK.value())
        .responseBody("Response body")
        .creationDatetime(LocalDateTime.now().toLocalDate().atStartOfDay())
        .build();
  }
}
