package com.apthletic.codingchallenge;

import com.apthletic.codingchallenge.entities.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TestUtils {

    public static String resourceToString(String resourceAsString) throws IOException {
        return resourceToString(new ClassPathResource(resourceAsString));
    }

    public static String resourceToString(ClassPathResource repositoryResource) throws IOException {
        try (InputStream inputStream = repositoryResource.getInputStream()) {
            byte[] fileBytes = StreamUtils.copyToByteArray(inputStream);
            return new String(fileBytes);
        }
    }

    public static AFLEvent randomAflEvent() {
        return randomAflEvent(OffsetDateTime.now());
    }

    public static AFLEvent randomAflEvent(OffsetDateTime time) {
        AFLEvent aflEvent = new AFLEvent();
        aflEvent.setId(RandomStringUtils.randomAlphanumeric(10));
        aflEvent.setTime(time);
        aflEvent.setAwayTeam(randomAflTeam());
        aflEvent.setHomeTeam(randomAflTeam());
        aflEvent.setAwayResult(randomAflResult());
        aflEvent.setHomeResult(randomAflResult());
        aflEvent.setRound(RandomStringUtils.randomNumeric(3));
        aflEvent.setStatus(EventStatus.NOT_STARTED);
        aflEvent.setVenue(RandomStringUtils.random(10));
        aflEvent.setTitle(RandomStringUtils.randomAlphabetic(10));
        return aflEvent;
    }

    public static AFLResult randomAflResult() {
        AFLResult aflResult = new AFLResult();
        aflResult.setBehinds(RandomUtils.nextInt(0, 10));
        aflResult.setGoals(RandomUtils.nextInt(0, 10));
        aflResult.setTotalScore(RandomUtils.nextInt(0, 100));
        return aflResult;
    }

    public static AFLTeam randomAflTeam() {
        AFLTeam aflTeam = new AFLTeam();
        aflTeam.setId(RandomStringUtils.randomAlphanumeric(4));
        aflTeam.setName(RandomStringUtils.randomAlphabetic(20));
        return aflTeam;
    }

    public static IronmanEvent randomIronmanEvent() {
        return randomIronmanEvent(OffsetDateTime.now());
    }

    public static IronmanEvent randomIronmanEvent(OffsetDateTime time) {
        IronmanEvent aflEvent = new IronmanEvent();
        aflEvent.setId(UUID.randomUUID());
        aflEvent.setCategory(RandomStringUtils.randomAlphabetic(10));
        aflEvent.setImage(RandomStringUtils.random(10));
        aflEvent.setLocation(RandomStringUtils.randomAlphabetic(10));
        aflEvent.setTime(time);
        aflEvent.setStatus(EventStatus.NOT_STARTED);
        aflEvent.setTitle(RandomStringUtils.randomAlphabetic(10));
        return aflEvent;
    }

}