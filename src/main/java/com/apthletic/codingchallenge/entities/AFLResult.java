

package com.apthletic.codingchallenge.entities;

import com.google.common.base.MoreObjects;

public class AFLResult {

    private int totalScore;
    private int behinds;
    private int goals;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getBehinds() {
        return behinds;
    }

    public void setBehinds(int behinds) {
        this.behinds = behinds;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("totalScore", totalScore)
                .toString();
    }
}
