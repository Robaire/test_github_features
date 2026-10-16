package com.example.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GreetingTest {
  private final Greeting greeting = new Greeting();

  @Test
  void helloIncludesTheGivenName() {
    assertEquals("Hello, Ada", greeting.hello("Ada"));
  }

  @Test
  void helloKeepsAnEmptyName() {
    assertEquals("Hello, ", greeting.hello(""));
  }
}
