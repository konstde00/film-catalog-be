package com.konstde00.filmcatalog.model.dto.reviews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PageableReviewItem {

    Long id;

    Long filmId;

    String createdBy;

    OffsetDateTime createdAt;

    Integer rating;

    String comment;
}
