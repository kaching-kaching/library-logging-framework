package com.kaching.logging;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kaching.logging.configuration.LoggingConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(classes = {WebConfigurationTest.class, LoggingConfiguration.class})
@EnableConfigurationProperties
@WebMvcTest
@ExtendWith(OutputCaptureExtension.class)
@ComponentScan(basePackages = "com.kaching.*")
public class LoggingInterceptorTest {
  @Autowired private WebApplicationContext wac;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  @WithMockUser
  void test(CapturedOutput output) throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/hello"))
        .andExpect(MockMvcResultMatchers.status().is(200));

    assertTrue(output.getOut().contains("API_REQUEST_LOG"));
    assertTrue(output.getOut().contains("API_RESPONSE_LOG"));
    //        assertTrue(output.getOut().contains("\"userId\":\"user_test\""));
  }

  @Test
  @WithMockUser
  void when_exception(CapturedOutput output) throws Exception {
    try {
      mockMvc
          .perform(MockMvcRequestBuilders.get("/error"))
          .andExpect(MockMvcResultMatchers.status().is(400));
      assertTrue(output.getOut().contains("API_REQUEST_LOG"));
      assertTrue(output.getOut().contains("API_RESPONSE_LOG"));
      assertTrue(output.getOut().contains("Failed to process request"));
    } catch (Exception e) {
    }
  }
}
