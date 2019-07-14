package com.apthletic.codingchallenge.entities;

import static com.apthletic.codingchallenge.TestUtils.randomAflEvent;

public class AFLEventTest extends BaseModelTest<AFLEvent> {

    public AFLEventTest() {
        super(AFLEvent.class, "afl_event.json");
    }

    @Override
    protected AFLEvent randomEvent() {
        return randomAflEvent();
    }

}