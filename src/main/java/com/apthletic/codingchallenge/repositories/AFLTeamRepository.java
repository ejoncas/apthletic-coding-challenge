package com.apthletic.codingchallenge.repositories;

import com.apthletic.codingchallenge.entities.AFLTeam;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for accessing {@link AFLTeam}'s stored in the database
 */
public interface AFLTeamRepository extends CrudRepository<AFLTeam, String> {

}