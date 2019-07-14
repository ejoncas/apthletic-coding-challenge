package com.apthletic.codingchallenge.web;

import com.apthletic.codingchallenge.entities.Event;
import com.apthletic.codingchallenge.entities.EventType;
import com.apthletic.codingchallenge.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * This controller is in charge of retrieving events from the database based on different search criteria.
 */
@RestController
public class EventController extends BaseController {

    @Autowired
    public EventController(List<EventService<?>> services) {
        super(services);
    }

    @GetMapping("/v1/events/{type}")
    public <T extends Event> Page<T> getUpcomingEvents(
            @PathVariable("type") EventType type,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime time,
            Pageable pageable) {
        OffsetDateTime startTime = time == null ? OffsetDateTime.now() : time;
        EventService<T> service = findService(type);
        return service.findEvents(startTime, pageable);
    }

}
