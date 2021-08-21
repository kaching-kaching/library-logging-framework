package com.kaching.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaching.logging.configuration.LoggingConfiguration;
import com.kaching.logging.util.LoggingConstant;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggingInterceptor implements HandlerInterceptor {

  private static final String LOG_REQUEST_ENTRY = "LOG_REQUEST_ENTRY";
  private static final String API_START_TIME = "API_START_TIME";
  private static final String TRACE_ID = "traceId";
  private static final String TOKEN_USER_ID = "user_id";
  private static final String HEADER_REG_UID = "X-ReqUID";
  private static final String X_FORWARDER_FOR = "X-FORWARDER_FOR";

  private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

  private LoggingConfiguration loggingConfiguration;
  private ObjectMapper objectMapper;

  public LoggingInterceptor(LoggingConfiguration loggingConfiguration, ObjectMapper objectMapper) {
    super();
    this.loggingConfiguration = loggingConfiguration;
    this.objectMapper = objectMapper;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    MDC.put(
        TRACE_ID,
        Optional.ofNullable(MDC.get(TRACE_ID))
            .orElse(
                Optional.ofNullable(request.getHeader(HEADER_REG_UID))
                    .orElse(UUID.randomUUID().toString())));

    request.setAttribute(API_START_TIME, System.currentTimeMillis());

    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      if (StringUtils.isNotBlank(loggingConfiguration.getType())) {
        LogEntry logEntry = createRequestLogEntry(request);
        request.setAttribute(LOG_REQUEST_ENTRY, logEntry);
        logger.info(
            LoggingConstant.REQUEST_LOG.getMessage(), objectMapper.writeValueAsString(logEntry));
      }
    }
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
      throws Exception {
    if (request.getAttribute(LOG_REQUEST_ENTRY) != null) {
      LogEntry entry = (LogEntry) request.getAttribute(LOG_REQUEST_ENTRY);
      LogEntry responseEntry = createResponseLogEntry(request, response, exception, entry);
      logger.info(
          LoggingConstant.RESPONSE_LOG.getMessage(),
          objectMapper.writeValueAsString(responseEntry));
    }
    MDC.clear();
    HandlerInterceptor.super.afterCompletion(request, response, handler, exception);
  }

  private LogEntry createResponseLogEntry(
      HttpServletRequest request,
      HttpServletResponse response,
      Exception exception,
      LogEntry entry) {
    LogEntry logEntry = constructResponseLogEntry(request, response, entry);

    if (exception != null) {
      entry.setErrorType(exception.getMessage());
    }

    return logEntry;
  }

  private LogEntry constructResponseLogEntry(
      HttpServletRequest request, HttpServletResponse response, LogEntry entry) {
    LogEntry logEntry = entry;
    logEntry.updateTimeStamp();
    logEntry.setMsgUid(UUID.randomUUID().toString());
    logEntry.setExecutionTime(calculateExecutionTime(request));
    logEntry.setStatusCode(String.valueOf(response.getStatus()));
    logEntry.setStatus(getEntryStatusFromHttp(response));
    return logEntry;
  }

  private String getEntryStatusFromHttp(HttpServletResponse response) {
    return HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()
        ? LoggingConstant.SUCCESS.getMessage()
        : LoggingConstant.FAILURE.getMessage();
  }

  private long calculateExecutionTime(HttpServletRequest request) {
    long startTime = (Long) request.getAttribute(API_START_TIME);
    long executionTime = System.currentTimeMillis() - startTime;
    return executionTime;
  }

  private LogEntry createRequestLogEntry(HttpServletRequest request) {
    LogEntry logEntry = constructRequestLogEntry(request);

    if (isUserAuthenticated()) {
      attachedUserIdInJwt(logEntry);
    }

    return logEntry;
  }

  private LogEntry constructRequestLogEntry(HttpServletRequest request) {
    LogEntry logEntry = LogEntry.initializedWithDefault();
    logEntry.setAppCode(loggingConfiguration.getAppCode());
    logEntry.setType(loggingConfiguration.getType());
    logEntry.setRequestType(request.getMethod());
    logEntry.setClientIp(getClientIp(request));
    logEntry.setTraceId(MDC.get(TRACE_ID));
    logEntry.setMsgUid(UUID.randomUUID().toString());
    return logEntry;
  }

  // TODO add userId to logEntry
  private void attachedUserIdInJwt(LogEntry logEntry) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof Jwt) {
      Jwt jwt = (Jwt) principal;
      logEntry.setUserId(jwt.getClaim(TOKEN_USER_ID));
    }
  }

  // TODO Validate token true --> add userId
  private boolean isUserAuthenticated() {
    return SecurityContextHolder.getContext() != null
        && SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }

  private String getClientIp(HttpServletRequest request) {
    final String ipFromHeader = request.getHeader(X_FORWARDER_FOR);

    if (ipFromHeader != null && ipFromHeader.length() > 0) {
      return ipFromHeader;
    }

    return request.getRemoteAddr();
  }
}
