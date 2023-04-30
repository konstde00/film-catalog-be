package com.konstde00.filmcatalog.controller;

import com.konstde00.filmcatalog.mapper.FilmMapper;
import com.konstde00.filmcatalog.mapper.ReviewMapper;
import com.konstde00.filmcatalog.model.dto.films.FilmWithReviewsDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmItem;
import com.konstde00.filmcatalog.model.dto.reviews.FilmReviewDto;
import com.konstde00.filmcatalog.model.entity.Review;
import com.konstde00.filmcatalog.model.enums.Genre;
import com.konstde00.filmcatalog.model.enums.ItemType;
import com.konstde00.filmcatalog.service.FileService;
import com.konstde00.filmcatalog.service.FilmService;
import com.konstde00.filmcatalog.service.ReviewService;
import com.konstde00.filmcatalog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/films")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmController {

    FilmService filmService;
    FileService fileService;
    UserService userService;
    ReviewService reviewService;

    @GetMapping("/v1")
    @Operation(summary = "Get list of films in collection")
    public ResponseEntity<PageableFilmDto> getFilms(@RequestParam(required = false) Long collectionId,
                                                    @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(required = false, defaultValue = "10") Integer itemsOnPage) {

        var films = filmService.getAll(collectionId, pageNumber, itemsOnPage);

        return ResponseEntity.ok(films);
    }

    @GetMapping("/v1/{id}")
    @Operation(summary = "Get item")
    public ResponseEntity<FilmWithReviewsDto> getById(@PathVariable Long id) {

        List<Review> reviews = reviewService.getAll(id);
        List<FilmReviewDto> filmReviews
                = ReviewMapper.toFilmReviewDtoList(reviews);

        var film = FilmMapper.toFilmFullDto(filmService.getById(id));
        film.setReviews(filmReviews);

        return ResponseEntity.ok(film);
    }

    @PatchMapping("/v1")
    @Operation(summary = "Update item")
    public ResponseEntity<PageableFilmItem> update(@RequestParam Long filmId,
                                                   @RequestParam String name,
                                                   @RequestParam Genre genre,
                                                   @RequestParam Integer durationMins,
                                                   @RequestParam String company,
                                                   @RequestParam String director,
                                                   @RequestParam String producers,
                                                   @RequestParam String writers,
                                                   @RequestParam String cast,
                                                   @RequestParam String trailerUrl,
                                                   @RequestParam String synopsis,
                                                   @RequestParam Integer completionYear,
                                                   MultipartFile photo) {

        var films = filmService.update(filmId, name, genre, durationMins, company, director,
                producers, writers, cast, trailerUrl, synopsis, completionYear, photo);

        return ResponseEntity.ok(films);
    }

    @PatchMapping("/v1/photo")
    @Operation(summary = "Update photo")
    public ResponseEntity<?> updateFilmPhoto(@RequestParam Long filmId,
                                             MultipartFile photo) {

        filmService.checkExistsById(filmId);

        fileService.updateItemPhoto(filmId, ItemType.FILM, photo);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/v1/collection")
    @Operation(summary = "Update item")
    public ResponseEntity<PageableFilmItem> updateFilmCollection(@RequestParam Long filmId,
                                                                 @RequestParam String collectionName) {

        var films = filmService.updateFilmCollection(filmId, collectionName);

        return ResponseEntity.ok(films);
    }

    @DeleteMapping("/v1/collection")
    @Operation(summary = "Update item")
    public ResponseEntity<PageableFilmItem> removeFilmCollection(@RequestParam Long filmId,
                                                                 @RequestParam Long collectionId) {

        var films = filmService.removeFilmCollection(filmId, collectionId);

        return ResponseEntity.ok(films);
    }

    @PostMapping("/v1")
    @Operation(summary = "Create item")
    public ResponseEntity<PageableFilmItem> create(@RequestParam String name,
                                                   @RequestParam Genre genre,
                                                   @RequestParam Integer durationMins,
                                                   @RequestParam String company,
                                                   @RequestParam String director,
                                                   @RequestParam String producers,
                                                   @RequestParam String writers,
                                                   @RequestParam String cast,
                                                   @RequestParam(required = false, defaultValue = "") String trailerUrl,
                                                   @RequestParam String synopsis,
                                                   @RequestParam Integer completionYear,
                                                   MultipartFile photo,
                                                   HttpServletRequest servletRequest) {

        var user = userService.getCurrentUser(servletRequest);

        var films = filmService.create(name, genre, durationMins, company, director,
                producers, writers, cast, trailerUrl, synopsis, completionYear, photo, user);

        return ResponseEntity.ok(films);
    }

    @DeleteMapping("/v1")
    @Operation(summary = "Delete item")
    public ResponseEntity<?> delete(@RequestParam Long id) {

        filmService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
