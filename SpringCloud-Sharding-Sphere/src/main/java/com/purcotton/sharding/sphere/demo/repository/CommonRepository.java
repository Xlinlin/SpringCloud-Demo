package com.purcotton.sharding.sphere.demo.repository;

import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/14 11:12
 * @since JDK 1.8
 */
public interface CommonRepository<T>
{
    void createTableIfNotExists();

    void dropTable();

    void truncateTable();

    Long insert(T entity);

    void delete(Long id);

    List<T> selectAll();

    List<T> selectRange();
}
