package com.konstde00.filmcatalog.service;

import com.konstde00.filmcatalog.mapper.FilmMapper;
import com.konstde00.filmcatalog.mapper.ReviewMapper;
import com.konstde00.filmcatalog.model.dto.common.PageableDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmItem;
import com.konstde00.filmcatalog.model.dto.reviews.FilmReviewDto;
import com.konstde00.filmcatalog.model.dto.reviews.PageableReviewItem;
import com.konstde00.filmcatalog.model.dto.reviews.PageableReviewsDto;
import com.konstde00.filmcatalog.model.entity.Film;
import com.konstde00.filmcatalog.model.entity.Review;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.repository.ReviewRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    FilmService filmService;
    ReviewRepository reviewRepository;

    public List<Review> getAll(Long filmId) {
        return reviewRepository.getAllByFilmId(filmId);
    }


    public PageableReviewsDto getAll(Long filmId, Integer pageNumber, Integer itemsOnPage) {

        Pageable pageRequest = PageRequest.of(pageNumber, itemsOnPage);

        PageableReviewsDto reviewsDto = new PageableReviewsDto();
        Page<Review> page = reviewRepository.findAllByFilmId(filmId, pageRequest);
        List<PageableReviewItem> data = ReviewMapper.toPageableItemList(page.getContent());

        boolean isFirst = (pageRequest.getPageNumber() == 0);
        boolean isLast = (pageRequest.getPageNumber() == page.getTotalPages() - 1);
        long totalElements = reviewRepository.count();

        reviewsDto.setData(data);
        PageableDto sorted = new PageableDto();
        sorted.setSort(pageRequest.getSort());
        sorted.setEmpty(data.isEmpty());
        sorted.setFirst(isFirst);
        sorted.setLast(isLast);
        sorted.setSize(page.getNumberOfElements());
        sorted.setPageNumber(page.getNumber());
        sorted.setTotalElements(totalElements);
        sorted.setTotalPages(page.getTotalPages());
        reviewsDto.setPageable(sorted);

        return reviewsDto;
    }

    public FilmReviewDto create(PageableReviewItem review, User creator) {

        Film film = filmService.getById(review.getFilmId());
        Review reviewEntity = ReviewMapper.toEntity(review);
        reviewEntity.setFilm(film);
        reviewEntity.setCreator(creator);
        Review savedReview = reviewRepository.save(reviewEntity);
        return ReviewMapper.toFilmReviewDto(savedReview);
    }
}
