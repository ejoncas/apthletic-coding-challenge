package com.apthletic.codingchallenge.web;

import com.apthletic.codingchallenge.TestUtils;
import com.apthletic.codingchallenge.entities.AFLEvent;
import com.apthletic.codingchallenge.entities.IronmanEvent;
import com.apthletic.codingchallenge.services.AFLEventService;
import com.apthletic.codingchallenge.services.IronmanEventService;
import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest extends BaseWebTest {

    @Autowired
    private AFLEventService aflEventService;
    @Autowired
    private IronmanEventService ironmanEventService;

    private static List<IronmanEvent> IRONMAN_EVENTS = ImmutableList.of(
            TestUtils.randomIronmanEvent(OffsetDateTime.now().minusDays(1)),
            TestUtils.randomIronmanEvent(OffsetDateTime.now().plusDays(1)),
            TestUtils.randomIronmanEvent(OffsetDateTime.now().plusDays(2))
    );
    private static List<AFLEvent> AFL_EVENTS = ImmutableList.of(
            TestUtils.randomAflEvent(OffsetDateTime.now().minusDays(1)),
            TestUtils.randomAflEvent(OffsetDateTime.now().plusDays(3)),
            TestUtils.randomAflEvent(OffsetDateTime.now().plusDays(4)),
            TestUtils.randomAflEvent(OffsetDateTime.now().plusDays(5))
    );

    @Before
    public void setupSampleEvents() {
        ironmanEventService.importEvents(IRONMAN_EVENTS);
        aflEventService.importEvents(AFL_EVENTS);
    }

    @After
    public void clearDB() {
        aflEventService.clearEvents();
        ironmanEventService.clearEvents();
    }

    @DataProvider
    public static Object[][] scenarios() {
        return new Object[][]{
                {"afl", AFL_EVENTS.size() - 1, AFL_EVENTS.size()},
                {"ironman", IRONMAN_EVENTS.size() - 1, IRONMAN_EVENTS.size()},

        };
    }

    @UseDataProvider("scenarios")
    @Test
    public void shouldRetrieveListOfEvents(String eventType, int sizeFromNow, int sizeFromBegginingOfTime) throws Exception {
        //Get without query parameter -> It should default to current time
        getMockMvc().perform(
                get("/v1/events/" + eventType)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(is(sizeFromNow)));

        //Get with query parameter -> It should use that time
        getMockMvc().perform(
                get("/v1/events/" + eventType)
                        .param("date", OffsetDateTime.now().minusYears(100).format(DateTimeFormatter.ISO_DATE_TIME))
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(is(sizeFromBegginingOfTime)));
    }

    @Test
    public void shouldReturn400OnInvalidEventType() throws Exception {
        getMockMvc().perform(
                get("/v1/events/myNewEventTypeNotSupportedYet")
        ).andExpect(status().isBadRequest());
    }

}