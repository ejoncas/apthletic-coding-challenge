package com.apthletic.codingchallenge.services;

import com.apthletic.codingchallenge.TestUtils;
import com.apthletic.codingchallenge.entities.IronmanEvent;
import com.apthletic.codingchallenge.repositories.IronmanEventRepository;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IronmanEventServiceTest {

    @Autowired
    private IronmanEventService eventService;
    @Autowired
    private IronmanEventRepository repository;
    @Autowired
    private ObjectMapper mapper;


    @Before
    @After
    public void restart() {
        repository.deleteAll();
    }

    @Test
    public void testShouldImportEventsAndHandleDuplicates() throws IOException {
        //Given: Some random  events
        List<IronmanEvent> randomEvents = ImmutableList.of(
                TestUtils.randomIronmanEvent(),
                TestUtils.randomIronmanEvent(),
                TestUtils.randomIronmanEvent(),
                mapper.readValue(TestUtils.resourceToString("ironman_event.json"), IronmanEvent.class)
        );
        //When: Save them all
        eventService.importEvents(randomEvents);

        //Then: Make sure they're in the database
        assertThat(repository.findAll(), IsIterableContainingInOrder.contains(randomEvents.toArray()));

        //Try to save the same event again
        IronmanEvent eventFromFile = mapper.readValue(TestUtils.resourceToString("ironman_event.json"),
                IronmanEvent.class);
        eventService.importEvents(ImmutableList.of(eventFromFile));

        //Then:
        assertThat(repository.findAll(), IsIterableContainingInOrder.contains(randomEvents.toArray()));
        //Then: Find by id  should return same
        Optional<IronmanEvent> byId = repository.findById(eventFromFile.getId());
        assertThat(byId.isPresent(), is(true));
        assertThat(byId.get(), is(eventFromFile));
    }


    @Test
    public void testSearchByStartTime() {
        //Given: Some events with different start time
        OffsetDateTime now = OffsetDateTime.now();

        IronmanEvent beforeNow = TestUtils.randomIronmanEvent(now.minusDays(1));
        IronmanEvent eventNow = TestUtils.randomIronmanEvent(now);
        IronmanEvent eventTomorrow = TestUtils.randomIronmanEvent(now.plusDays(1));
        IronmanEvent eventIn2Weeks = TestUtils.randomIronmanEvent(now.plusDays(15));

        List<IronmanEvent> all = ImmutableList.of(beforeNow, eventNow, eventTomorrow, eventIn2Weeks);
        eventService.importEvents(all);

        //Then: repository should filter them  properly
        List<IronmanEvent> afterNowResult = eventService.findEvents(now, PageRequest.of(0, 10)).getContent();
        assertThat(afterNowResult, IsCollectionContaining.hasItems(eventNow, eventTomorrow, eventIn2Weeks));

        List<IronmanEvent> atTomorrowResult = eventService.findEvents(now.plusDays(1), PageRequest.of(0, 10)).getContent();
        assertThat(atTomorrowResult, IsCollectionContaining.hasItems(eventTomorrow, eventIn2Weeks));

        List<IronmanEvent> afterTomorrowResult = eventService.findEvents(now.plusDays(2), PageRequest.of(0, 10)).getContent();
        assertThat(afterTomorrowResult, IsCollectionContaining.hasItems(eventIn2Weeks));

        List<IronmanEvent> afterOneYearAgoResult = eventService.findEvents(now.minusYears(1), PageRequest.of(0, 10)).getContent();
        assertThat(afterOneYearAgoResult, IsCollectionContaining.hasItems(beforeNow, eventNow, eventTomorrow, eventIn2Weeks));
    }


}