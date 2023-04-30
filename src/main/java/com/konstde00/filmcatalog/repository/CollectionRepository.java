package com.konstde00.filmcatalog.repository;

import com.konstde00.filmcatalog.model.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("select case when count(c) > 0 then true else false end from Collection c where c.name = :name")
    Boolean existsByName(String name);

    @Query("select c from Collection c left join fetch c.films where c.id = ?1")
    Optional<Collection> findByIdWithFilms(@Param("id") Long id);

    @Query("select c from Collection c left join fetch c.films where c.name = ?1")
    Optional<Collection> findByName(@Param("name") String name);
}
