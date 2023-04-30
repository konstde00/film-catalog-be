package com.konstde00.filmcatalog.controller;

import com.konstde00.filmcatalog.model.dto.reviews.FilmReviewDto;
import com.konstde00.filmcatalog.model.dto.reviews.PageableReviewItem;
import com.konstde00.filmcatalog.model.dto.reviews.PageableReviewsDto;
import com.konstde00.filmcatalog.service.ReviewService;
import com.konstde00.filmcatalog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {

    UserService userService;
    ReviewService reviewService;

    @GetMapping("/v1")
    @Operation(summary = "Get list of reviews for a film")
    public ResponseEntity<PageableReviewsDto> getFilms(@RequestParam Long filmId,
                                                       @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                       @RequestParam(value = "itemsOnPage", required = false, defaultValue = "10") Integer itemsOnPage) {

        var reviews = reviewService.getAll(filmId, pageNumber, itemsOnPage);

        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/v1")
    @Operation(summary = "Create review")
    public ResponseEntity<FilmReviewDto> createReview(@RequestBody PageableReviewItem review,
                                                      HttpServletRequest request) {

        var user = userService.getCurrentUser(request);

        var createdReview = reviewService.create(review, user);

        return ResponseEntity.ok(createdReview);
    }
}
