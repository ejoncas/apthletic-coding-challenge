package com.apthletic.codingchallenge.services;

import com.apthletic.codingchallenge.entities.Event;
import com.apthletic.codingchallenge.entities.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Event Service interface
 *
 * @param <T> the event type
 */
public interface EventService<T extends Event> {

    /**
     * Imports events from an external source. Usually the  crawlers will call this.
     * <p>
     * This method is supposed to deal with  duplicates and data already  stored in the  database.
     *
     * @param events the list of events to import.
     */
    void importEvents(List<T> events);

    /**
     * Retrieves events starting  on startTime or after.
     * <p>
     * Results are paginated. Use the pageable parameter to specify different pages or sort orders.
     *
     * @param startTime the start time to consider events from
     * @param pageable  the pageable parameters (page, pageSize, order, etc)
     * @return the page of results
     */
    Page<T> findEvents(OffsetDateTime startTime, Pageable pageable);

    /**
     * The type of events that this service operates with
     *
     * @return the {@link EventType}
     */
    EventType getType();

}
