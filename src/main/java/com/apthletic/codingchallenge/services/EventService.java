package com.apthletic.codingchallenge.services;

import com.apthletic.codingchallenge.entities.Event;
import com.apthletic.codingchallenge.entities.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public interface EventService<T extends Event> {

    void importEvents(List<T> events);

    Page<T> findEvents(OffsetDateTime startTime, Pageable pageable);

    EventType getType();

}
