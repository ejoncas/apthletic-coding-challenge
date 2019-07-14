package com.apthletic.codingchallenge.repositories;

import com.apthletic.codingchallenge.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.OffsetDateTime;

@NoRepositoryBean
public interface BaseEventRepository<T extends Event, ID extends Serializable> extends CrudRepository<T, ID> {

    Page<T> findByTimeGreaterThanEqualOrderByTimeAsc(OffsetDateTime dateTime, Pageable pageable);

}
