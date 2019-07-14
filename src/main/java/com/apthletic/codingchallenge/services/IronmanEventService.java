package com.apthletic.codingchallenge.services;

import com.apthletic.codingchallenge.entities.EventType;
import com.apthletic.codingchallenge.entities.IronmanEvent;
import com.apthletic.codingchallenge.repositories.IronmanEventRepository;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Ironman Event Service
 * <p>
 * This  service is responsible of implementing all the business logic around  storing and retrieving Ironman Events
 */
@Service
public class IronmanEventService implements EventService<IronmanEvent> {

    @Autowired
    private IronmanEventRepository repository;

    @Override
    public void importEvents(List<IronmanEvent> events) {
        repository.saveAll(events);
    }

    @VisibleForTesting
    public void clearEvents() {
        repository.deleteAll();
    }

    @Override
    public Page<IronmanEvent> findEvents(OffsetDateTime startTime, Pageable pageable) {
        return repository.findByTimeGreaterThanEqualOrderByTimeAsc(startTime, pageable);
    }

    @Override
    public EventType getType() {
        return EventType.ironman;
    }
}
