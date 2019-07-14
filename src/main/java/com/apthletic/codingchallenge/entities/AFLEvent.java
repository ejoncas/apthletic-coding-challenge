
package com.apthletic.codingchallenge.entities;

import com.google.common.base.MoreObjects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class AFLEvent extends Event {

    @Id
    private String id;
    private String venue;
    private String round;

    @ManyToOne
    private AFLTeam homeTeam;
    @ManyToOne
    private AFLTeam awayTeam;

    @Embedded
    private AFLResult homeResult;
    @Embedded
    private AFLResult awayResult;

    public AFLEvent() {
        super(EventType.afl);
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public AFLTeam getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(AFLTeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    public AFLTeam getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(AFLTeam awayTeam) {
        this.awayTeam = awayTeam;
    }

    public AFLResult getHomeResult() {
        return homeResult;
    }

    public void setHomeResult(AFLResult homeResult) {
        this.homeResult = homeResult;
    }

    public AFLResult getAwayResult() {
        return awayResult;
    }

    public void setAwayResult(AFLResult awayResult) {
        this.awayResult = awayResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AFLEvent aflEvent = (AFLEvent) o;
        return Objects.equals(id, aflEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
