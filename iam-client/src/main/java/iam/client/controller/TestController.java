package iam.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(@RequestParam String name) {
      log.info("8080 test : ", name);
      return name;
    }
}
