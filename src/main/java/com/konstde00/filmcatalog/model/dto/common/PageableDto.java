package com.konstde00.filmcatalog.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PageableDto {

    Sort sort;

    boolean empty;

    boolean first;

    boolean last;

    Integer size;

    Integer pageNumber;

    Long totalElements;

    Integer totalPages;
}
