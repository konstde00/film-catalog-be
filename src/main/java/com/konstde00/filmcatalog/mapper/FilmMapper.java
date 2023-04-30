package com.konstde00.filmcatalog.mapper;

import com.konstde00.filmcatalog.model.dto.films.FilmWithReviewsDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmItem;
import com.konstde00.filmcatalog.model.entity.Film;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FilmMapper {

    public static List<PageableFilmItem> toPageableItemList(List<Film> films) {
        return films == null ? List.of( ): films.stream()
                .map(FilmMapper::toPageableItem)
                .collect(Collectors.toList());
    }

    public static PageableFilmItem toPageableItem(Film film) {
        return new PageableFilmItem()
                .builder()
                .id(film.getId())
                .name(film.getName())
                .genre(film.getGenre() == null ? null : film.getGenre().getValue())
                .durationMins(film.getDurationMins())
                .company(film.getCompany())
                .director(film.getDirector())
                .producers(film.getProducers())
                .writers(film.getWriters())
                .cast(film.getCast())
                .trailerUrl(film.getTrailerUrl())
                .synopsis(film.getSynopsis())
                .completionYear(film.getCompletionYear())
                .photoUrl("https://filmcatalog.s3.amazonaws.com/films/" + film.getId() + "/photo")
                .lastUpdate(Objects.requireNonNullElse(film.getUpdatedAt(),
                        film.getCreatedAt()))
                .build();
    }

    public static FilmWithReviewsDto toFilmFullDto(Film film) {

        return new FilmWithReviewsDto()
                .builder()
                .id(film.getId())
                .name(film.getName())
                .genre(film.getGenre() == null ? null : film.getGenre().getValue())
                .durationMins(film.getDurationMins())
                .company(film.getCompany())
                .director(film.getDirector())
                .producers(film.getProducers())
                .writers(film.getWriters())
                .cast(film.getCast())
                .trailerUrl(film.getTrailerUrl())
                .synopsis(film.getSynopsis())
                .completionYear(film.getCompletionYear())
                .photoUrl("https://filmcatalog.s3.amazonaws.com/films/" + film.getId() + "/photo")
                .build();
    }
}
