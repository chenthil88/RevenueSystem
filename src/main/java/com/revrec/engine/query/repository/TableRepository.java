package com.revrec.engine.query.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD-style access for a single table via the query engine.
 */
public interface TableRepository<R, ID> {

    Optional<R> findById(ID id);

    List<R> findAll(int limit, int offset);
}
