package com.apthletic.codingchallenge.services;

import com.apthletic.codingchallenge.entities.AFLEvent;
import com.apthletic.codingchallenge.entities.AFLTeam;
import com.apthletic.codingchallenge.entities.EventType;
import com.apthletic.codingchallenge.repositories.AFLEventRepository;
import com.apthletic.codingchallenge.repositories.AFLTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Service
public class AFLEventService implements EventService<AFLEvent> {

    @Autowired
    private AFLEventRepository repository;
    @Autowired
    private AFLTeamRepository teamRepository;

    @Override
    public void importEvents(List<AFLEvent> events) {
        saveTeams(events);
        saveEvents(events);
    }

    @Override
    public Page<AFLEvent> findEvents(OffsetDateTime startTime, Pageable pageable) {
        return repository.findByTimeGreaterThanEqualOrderByTimeAsc(startTime, pageable);
    }

    /**
     * Saves  the afl events
     *
     * @param events
     */
    private void saveEvents(List<AFLEvent> events) {
        repository.saveAll(events);
    }

    /**
     * Extracts all teams and stores them in the dabatase.
     * If they already exists, they will be updated.
     *
     * @param events
     */
    private void saveTeams(List<AFLEvent> events) {
        Set<AFLTeam> teams = events.stream()
                .flatMap(e -> Stream.of(e.getAwayTeam(), e.getHomeTeam()))
                .collect(toSet());
        teamRepository.saveAll(teams);
    }

    @Override
    public EventType getType() {
        return EventType.afl;
    }
}
