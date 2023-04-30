package com.konstde00.filmcatalog.model.dto.films;

import com.konstde00.filmcatalog.model.dto.common.PageableDto;
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
public class PageableFilmDto {

    List<PageableFilmItem> data;

    PageableDto pageable;
}
