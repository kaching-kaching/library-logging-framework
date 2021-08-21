package com.kaching.logging;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {
  @Override
  public SecurityContext createSecurityContext(WithMockUser withMockUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Jwt jwt =
        Jwt.withTokenValue("abc").claim("user_id", "user_test").header("alg", "ES256").build();
    context.setAuthentication(new TestingAuthenticationToken(jwt, jwt, "ROLE_USER"));
    return context;
  }
}
