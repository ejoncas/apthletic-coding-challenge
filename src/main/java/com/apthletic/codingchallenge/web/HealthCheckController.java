/*
 * Copyright (c) Message4U Pty Ltd 2014-2019
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.apthletic.codingchallenge.web;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller to make sure the  application is alive.
 * <p>
 * This is the first endpoint I created to test the project configuration, later on this can be replaced with
 * spring actuator or a more useful healthcheck that returns stats or actually tries to ping the database.
 */
@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return ImmutableMap.of("status", "OK");
    }

}
