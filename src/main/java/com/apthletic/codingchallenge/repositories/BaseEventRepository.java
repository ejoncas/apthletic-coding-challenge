package com.apthletic.codingchallenge.repositories;

import com.apthletic.codingchallenge.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Base event repository that applies to every event  that  extends from {@link Event}
 * <p>
 * This is the place to add any  findBy*  that operates with fields from the base class
 *
 * @param <T>  the type
 * @param <ID> the id of the  entity
 */
@NoRepositoryBean
public interface BaseEventRepository<T extends Event, ID extends Serializable> extends CrudRepository<T, ID> {

    Page<T> findByTimeGreaterThanEqualOrderByTimeAsc(OffsetDateTime dateTime, Pageable pageable);

}
