package com.example.app;

public final class Greeting {
  public String hello(String name) {
    return "Hello, " + name;
  }

  public static void main(String[] args) {
    String name = args.length > 0 ? args[0] : "world";
    System.out.println(new Greeting().hello(name));
  }
}
