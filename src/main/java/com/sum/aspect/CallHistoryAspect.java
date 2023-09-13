package com.sum.aspect;

import com.sum.dto.CallHistoryResponseDTO;
import com.sum.kafka.CallHistoryEventProducer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
@Component
@AllArgsConstructor
@Slf4j
public class CallHistoryAspect {
  private final CallHistoryEventProducer kafkaHistoryService;

  @AfterReturning(pointcut = "execution(* com.sum.controller..*.*(..))", returning = "result")
  @Async("asyncExecutor")
  public void saveCallHistory(JoinPoint joinPoint, Object result) {
    if (result instanceof ResponseEntity<?> responseEntity
        && responseEntity.getStatusCode().is2xxSuccessful()) {

      log.info("Obtaining information from the successful request.");
      try {
        var methodName = joinPoint.getSignature().getName();
        var requestMethod = getRequestMethod(joinPoint);
        var requestPath = getRequestPath(joinPoint);
        var responseStatus = responseEntity.getStatusCodeValue();
        var responseBody = getResponseBody(responseEntity);

        log.info("The information was successfully obtained");
        kafkaHistoryService.sendCall(
            new CallHistoryResponseDTO(
                methodName,
                requestMethod,
                requestPath,
                responseStatus,
                responseBody,
                LocalDateTime.now()));
      } catch (Exception e) {
        log.error("An error occurred while obtaining the request information.");
      }
    }
  }

  private String getRequestMethod(JoinPoint joinPoint) {
    var methodSignature = (MethodSignature) joinPoint.getSignature();
    var method = methodSignature.getMethod();
    var mapping =
        Map.of(
            GetMapping.class, "GET",
            PostMapping.class, "POST",
            PutMapping.class, "PUT",
            DeleteMapping.class, "DELETE",
            PatchMapping.class, "PATCH");
    return mapping.entrySet().stream()
        .filter(entry -> method.isAnnotationPresent(entry.getKey()))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse("UNKNOWN");
  }

  private String getRequestPath(JoinPoint joinPoint) {
    var methodSignature = (MethodSignature) joinPoint.getSignature();
    var method = methodSignature.getMethod();
    var targetClass = method.getDeclaringClass();
    var classMapping = getClassAnnotationValue(targetClass);
    var methodMapping = getMethodAnnotationValue(method);
    var url = new StringBuilder(classMapping);

    if (!methodMapping.isEmpty()) {
      url.append(methodMapping);
    }

    var args = joinPoint.getArgs();
    if (args.length > 0) {
      url.append("?");
      var parameters =
          IntStream.range(0, args.length)
              .mapToObj(
                  i -> {
                    var paramName = getParameterNames(method)[i];
                    var paramValue = args[i].toString();
                    return paramName + "=" + paramValue;
                  })
              .collect(Collectors.joining("&"));

      url.append(parameters);
    }

    return url.toString();
  }

  private String[] getParameterNames(Method method) {
    var parameters = method.getParameters();
    var parameterNames = new String[parameters.length];
    Arrays.setAll(parameterNames, i -> parameters[i].getName());
    return parameterNames;
  }

  private String getClassAnnotationValue(Class<?> targetClass) {
    return Arrays.stream(
            targetClass.getAnnotationsByType((Class<? extends Annotation>) RequestMapping.class))
        .map(annotation -> ((RequestMapping) annotation).value())
        .filter(values -> values.length > 0)
        .map(values -> values[0])
        .findFirst()
        .orElse("");
  }

  private String getMethodAnnotationValue(Method method) {
    return Arrays.stream(
            method.getAnnotationsByType((Class<? extends Annotation>) RequestMapping.class))
        .map(annotation -> ((RequestMapping) annotation).value())
        .filter(values -> values.length > 0)
        .map(values -> values[0])
        .findFirst()
        .orElse("");
  }

  private String getResponseBody(ResponseEntity<?> responseEntity) {
    var body = responseEntity.getBody();
    if (body instanceof Page<?>) {
      body = ((Page<?>) body).getContent();
    }
    return (body != null) ? body.toString() : "";
  }
}
