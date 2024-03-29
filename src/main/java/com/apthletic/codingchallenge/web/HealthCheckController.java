package com.apthletic.codingchallenge.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to make sure the  application is alive.
 * <p>
 * This is the first endpoint I created to test the project configuration, later on this can be replaced with
 * spring actuator or a more useful healthcheck that returns stats or actually tries to ping the database.
 */
@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public StatusResponse health() {
        return StatusResponse.of("I'm alive!");
    }

}
