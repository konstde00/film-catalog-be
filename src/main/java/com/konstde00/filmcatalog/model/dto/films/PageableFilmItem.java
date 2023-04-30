package com.konstde00.filmcatalog.model.dto.films;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PageableFilmItem {

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

    LocalDateTime lastUpdate;
}
