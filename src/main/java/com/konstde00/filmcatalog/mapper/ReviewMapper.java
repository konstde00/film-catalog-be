package com.konstde00.filmcatalog.mapper;

import com.konstde00.filmcatalog.model.dto.reviews.FilmReviewDto;
import com.konstde00.filmcatalog.model.dto.reviews.PageableReviewItem;
import com.konstde00.filmcatalog.model.entity.Review;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {

    public static List<PageableReviewItem> toPageableItemList(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewMapper::toPageableItem)
                .collect(Collectors.toList());
    }

    public static PageableReviewItem toPageableItem(Review review) {
        return new PageableReviewItem()
                .builder()
                .id(review.getId())
                .comment(review.getComment())
                .createdBy(review.getCreator().getName())
                .createdAt(OffsetDateTime.of(review.getCreatedAt(), ZoneOffset.UTC))
                .rating(review.getRating())
                .build();
    }

    public static FilmReviewDto toFilmReviewDto(Review review) {
        return new FilmReviewDto()
                .builder()
                .userId(review.getCreator().getId())
                .userName(review.getCreator().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }

    public static List<FilmReviewDto> toFilmReviewDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewMapper::toFilmReviewDto)
                .collect(Collectors.toList());
    }

    public static Review toEntity(PageableReviewItem review) {
        return new Review()
                .builder()
                .comment(review.getComment())
                .rating(review.getRating())
                .build();
    }
}
