package com.sum.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "call_history")
@Setter
@Getter
public class CallHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "method_name", nullable = false)
  private String methodName;

  @Column(name = "request_method", nullable = false)
  private String requestMethod;

  @Column(name = "request_path", nullable = false)
  private String requestPath;

  @Column(name = "response_status", nullable = false)
  private Integer responseStatus;

  @Column(name = "response_body")
  private String responseBody;

  @Column(name = "creation_datetime", nullable = false)
  private LocalDateTime creationDatetime;
}
