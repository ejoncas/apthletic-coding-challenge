package com.apthletic.codingchallenge.services;

import com.apthletic.codingchallenge.TestUtils;
import com.apthletic.codingchallenge.entities.AFLEvent;
import com.apthletic.codingchallenge.repositories.AFLEventRepository;
import com.apthletic.codingchallenge.repositories.AFLTeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AFLEventServiceTest {

    @Autowired
    private AFLEventService eventService;
    @Autowired
    private AFLEventRepository eventRepository;
    @Autowired
    private AFLTeamRepository teamRepository;
    @Autowired
    private ObjectMapper mapper;

    @Before
    @After
    public void restart() {
        eventService.clearEvents();
    }

    @Test
    public void testShouldImportEventsAndHandleDuplicates() throws IOException {
        //Given: Some random  events
        List<AFLEvent> randomEvents = ImmutableList.of(
                TestUtils.randomAflEvent(),
                TestUtils.randomAflEvent(),
                TestUtils.randomAflEvent(),
                mapper.readValue(TestUtils.resourceToString("afl_event.json"), AFLEvent.class)
        );
        //When: Save them all
        eventService.importEvents(randomEvents);

        //Then: Make sure they're in the database
        assertThat(eventRepository.findAll(), IsIterableContainingInOrder.contains(randomEvents.toArray()));
        //Make sure it saved the  teams too!
        assertThat(ImmutableList.of(teamRepository.findAll()).size(), is(greaterThan(0)));

        //Try to save the same event again
        AFLEvent eventFromFile = mapper.readValue(TestUtils.resourceToString("afl_event.json"),
                AFLEvent.class);
        eventService.importEvents(ImmutableList.of(eventFromFile));

        //Then:
        assertThat(eventRepository.findAll(), IsIterableContainingInOrder.contains(randomEvents.toArray()));
        //Then: Find by id  should return same
        Optional<AFLEvent> byId = eventRepository.findById(eventFromFile.getId());
        assertThat(byId.isPresent(), is(true));
        assertThat(byId.get(), is(eventFromFile));
    }


    @Test
    public void testSearchByStartTime() {
        //Given: Some events with different start time
        OffsetDateTime now = OffsetDateTime.now();

        AFLEvent beforeNow = TestUtils.randomAflEvent(now.minusDays(1));
        AFLEvent eventNow = TestUtils.randomAflEvent(now);
        AFLEvent eventTomorrow = TestUtils.randomAflEvent(now.plusDays(1));
        AFLEvent eventIn2Weeks = TestUtils.randomAflEvent(now.plusDays(15));

        List<AFLEvent> all = ImmutableList.of(beforeNow, eventNow, eventTomorrow, eventIn2Weeks);
        eventService.importEvents(all);

        //Then: repository should filter them  properly
        List<AFLEvent> afterNowResult = eventService.findEvents(now, PageRequest.of(0, 10)).getContent();
        assertThat(afterNowResult, IsCollectionContaining.hasItems(eventNow, eventTomorrow, eventIn2Weeks));

        List<AFLEvent> atTomorrowResult = eventService.findEvents(now.plusDays(1), PageRequest.of(0, 10)).getContent();
        assertThat(atTomorrowResult, IsCollectionContaining.hasItems(eventTomorrow, eventIn2Weeks));

        List<AFLEvent> afterTomorrowResult = eventService.findEvents(now.plusDays(2), PageRequest.of(0, 10)).getContent();
        assertThat(afterTomorrowResult, IsCollectionContaining.hasItems(eventIn2Weeks));

        List<AFLEvent> afterOneYearAgoResult = eventService.findEvents(now.minusYears(1), PageRequest.of(0, 10)).getContent();
        assertThat(afterOneYearAgoResult, IsCollectionContaining.hasItems(beforeNow, eventNow, eventTomorrow, eventIn2Weeks));
    }


}