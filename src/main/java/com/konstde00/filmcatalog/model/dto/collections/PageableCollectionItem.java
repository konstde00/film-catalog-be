package com.konstde00.filmcatalog.model.dto.collections;

import com.konstde00.filmcatalog.model.dto.films.PageableFilmItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PageableCollectionItem {

    Long id;

    String name;
    String description;
    String photoUrl;
    List<PageableFilmItem> films;
    LocalDateTime lastUpdate;
}
