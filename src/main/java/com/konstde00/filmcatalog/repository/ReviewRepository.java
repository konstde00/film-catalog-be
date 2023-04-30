package com.konstde00.filmcatalog.repository;

import com.konstde00.filmcatalog.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.film.id = ?1")
    List<Review> getAllByFilmId(Long filmId);

    @Query("SELECT r FROM Review r WHERE r.film.id = ?1")
    Page<Review> findAllByFilmId(Long filmId, Pageable pageRequest);
}
