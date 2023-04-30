package com.konstde00.filmcatalog.repository;

import com.konstde00.filmcatalog.model.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("select case when count(f) > 0 then true else false end from Film f where f.name = :name")
    Boolean existsByName(String name);

    @Query("select f from Film f left join fetch f.collections where f.id = :id")
    Optional<Film> findByIdWithCollections(Long id);
}
