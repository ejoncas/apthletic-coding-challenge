package com.apthletic.codingchallenge.web;

import com.apthletic.codingchallenge.entities.Event;
import com.apthletic.codingchallenge.entities.EventType;
import com.apthletic.codingchallenge.services.EventService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public abstract class BaseController {

    private final Map<EventType, EventService<?>> importers;

    protected BaseController(List<EventService<?>> eventServices) {
        this.importers = eventServices.stream()
                .collect(toMap(EventService::getType, Function.identity()));
    }

    protected <T extends Event> EventService<T> findService(EventType type) {
        EventService<T> eventService = (EventService<T>) importers.get(type);
        if (eventService == null) {
            throw new IllegalArgumentException("Do not know how to import events of type " + type + " yet");
        }
        return eventService;
    }
}
