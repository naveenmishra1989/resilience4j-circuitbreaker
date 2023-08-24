package com.naveen.circuitbreaker.controller;

import com.naveen.circuitbreaker.model.Activity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequestMapping("/activity")
@RestController
public class ActivityController {

    private RestTemplate restTemplate;

    private final String BORED_API = "https://www.boredapi.com/api/activity";

    public ActivityController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
   // @CircuitBreaker(name = "randomActivity", fallbackMethod = "fallbackRandomActivity")
    @Retry(name = "default", fallbackMethod = "fallbackRandomActivity")
    public String getRandomActivity() {
        log.info("Activity ");
        ResponseEntity<Activity> responseEntity = restTemplate.getForEntity(BORED_API, Activity.class);
        Activity activity = responseEntity.getBody();
        log.info("Activity received: " + activity.getActivity());
        return activity.getActivity();
    }

    public String fallbackRandomActivity(Throwable throwable) {
        log.error(throwable.getMessage());
        return "Please visit github.com/naveenjava";
    }

}

