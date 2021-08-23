package com.kaching.logging;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
  @GetMapping("hello")
  public String hello() {
    return "hello";
  }

  @GetMapping("error")
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public String error() {
    throw new RuntimeException("Failed to process request");
  }
}
