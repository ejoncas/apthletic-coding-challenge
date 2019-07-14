package com.apthletic.codingchallenge.entities;

import com.apthletic.codingchallenge.TestUtils;

public class AFLTeamTest extends BaseModelTest<AFLTeam> {

    public AFLTeamTest() {
        super(AFLTeam.class, "afl_team.json");
    }

    @Override
    protected AFLTeam randomEvent() {
        return TestUtils.randomAflTeam();
    }

}