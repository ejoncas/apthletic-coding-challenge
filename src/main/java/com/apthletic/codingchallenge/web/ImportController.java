package com.apthletic.codingchallenge.web;

import com.apthletic.codingchallenge.entities.Event;
import com.apthletic.codingchallenge.entities.EventType;
import com.apthletic.codingchallenge.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller in charge of importing events to the system
 */
@RestController
public class ImportController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    public ImportController(List<EventService<?>> eventServices) {
        super(eventServices);
    }

    @PostMapping("/v1/import/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    public <T extends Event> StatusResponse importEvents(@PathVariable("type") EventType type,
                                                         @RequestBody List<T> events) {
        LOGGER.info("Importing events for type {} - Data: {}", type, events);
        EventService<T> service = findService(type);
        service.importEvents(events);
        return StatusResponse.of("Imported " + events.size() + " events");
    }


}
