package com.washinggod.remkey.configuration.filter;

import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.service.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

  @Autowired private RateLimitService rateLimitService;

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  /// // to push exception to GlobalExceptionHandler

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {

      String userId = auth.getName();
      Bucket bucket = rateLimitService.resolveBucket(userId);

      //                bucket consume per time
      if (!bucket.tryConsume(1)) {
        log.warn("UserId {} has been locked because request too much", userId);
        resolver.resolveException(
            request, response, null, new AppException(ErrorCode.TOO_MANY_REQUEST));
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
