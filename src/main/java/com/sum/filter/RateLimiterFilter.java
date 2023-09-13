package com.sum.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sum.exception.ErrorResponse;
import io.github.bucket4j.Bucket;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
@Slf4j
public class RateLimiterFilter extends OncePerRequestFilter {

  private final Bucket bucket;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    if (bucket.tryConsume(1)) {
      filterChain.doFilter(request, response);
    } else {
      log.error("Request rate limited: {}", request.getRequestURI());
      var errorResponse =
          new ErrorResponse(
              HttpStatus.TOO_MANY_REQUESTS.name(), "Exceeded the number of requests per minute");
      var objectMapper = new ObjectMapper();
      var errorResponseJson = objectMapper.writeValueAsString(errorResponse);
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.setContentType(MediaType.APPLICATION_JSON.toString());
      response.getWriter().write(errorResponseJson);
    }
  }
}
