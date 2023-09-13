package com.sum.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallHistoryResponseDTO implements Serializable {
  private String methodName;
  private String requestMethod;
  private String requestPath;
  private Integer responseStatus;
  private String responseBody;
  private LocalDateTime creationDatetime;
}
