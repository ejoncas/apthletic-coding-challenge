package com.apthletic.codingchallenge.entities;

import com.apthletic.codingchallenge.TestUtils;

public class IronmanEventTest extends BaseModelTest<IronmanEvent> {

    public IronmanEventTest() {
        super(IronmanEvent.class, "ironman_event.json");
    }

    @Override
    protected IronmanEvent randomEvent() {
        return TestUtils.randomIronmanEvent();
    }

}