package com.konstde00.filmcatalog.model.dto.films;

import com.konstde00.filmcatalog.model.dto.reviews.FilmReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FilmWithReviewsDto {

    Long id;

    String name;

    String genre;

    Integer durationMins;

    String company;

    String director;

    String producers;

    String writers;

    String cast;

    String trailerUrl;

    String synopsis;

    Integer completionYear;

    String photoUrl;

    List<FilmReviewDto> reviews;
}
