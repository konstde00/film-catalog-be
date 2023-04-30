package com.konstde00.filmcatalog.service;

import com.konstde00.filmcatalog.mapper.FilmMapper;
import com.konstde00.filmcatalog.model.dto.common.PageableDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmItem;
import com.konstde00.filmcatalog.model.entity.Collection;
import com.konstde00.filmcatalog.model.entity.Film;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.model.enums.Genre;
import com.konstde00.filmcatalog.model.enums.ItemType;
import com.konstde00.filmcatalog.model.exception.NotValidException;
import com.konstde00.filmcatalog.repository.FilmRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmService {

    FileService fileService;
    FilmRepository filmRepository;
    CollectionService collectionService;

    public Film getById(Long id) {

        return filmRepository.findById(id).orElseThrow(() ->
                new NotValidException("Film with id " + id + " not found"));
    }

    public Film getByIdWithCollections(Long id) {
        return filmRepository.findByIdWithCollections(id).orElseThrow(() ->
                new NotValidException("Film with id " + id + " not found"));
    }

    public void checkExistsById(Long id) {

        if (!filmRepository.existsById(id)) {
            throw new NotValidException("Film with id " + id + " not found");
        }
    }

    public void checkExistsByName(String name) {

            if (filmRepository.existsByName(name)) {
                throw new NotValidException("Film with name '" + name + "' already exists");
            }
    }

    public PageableFilmItem create(String name,
                                   Genre genre,
                                   Integer durationMins,
                                   String company,
                                   String director,
                                   String producers,
                                   String writers,
                                   String cast,
                                   String trailerUrl,
                                   String synopsis,
                                   Integer completionYear,
                                   MultipartFile file,
                                   User user) {

        checkExistsByName(name);

        Film film = Film.builder()
                .name(name)
                .genre(genre)
                .durationMins(durationMins)
                .company(company)
                .director(director)
                .producers(producers)
                .writers(writers)
                .cast(cast)
                .trailerUrl(trailerUrl)
                .synopsis(synopsis)
                .completionYear(completionYear)
                .build();
        film.setCreatedBy(user);

        film = filmRepository.save(film);

        if (file == null) {
            fileService.uploadDefaultImage(film.getId(), ItemType.FILM);
        } else {
            fileService.updateItemPhoto(film.getId(), ItemType.FILM, file);
        }

        return FilmMapper.toPageableItem(film);
    }

    public PageableFilmDto getAll(Long collectionId, Integer pageNumber, Integer itemsOnPage) {

        Pageable pageRequest = PageRequest.of(pageNumber, itemsOnPage);

        PageableFilmDto filmDto = new PageableFilmDto();
        Page<Film> page = filmRepository.findAll(pageRequest);
        List<PageableFilmItem> data = FilmMapper.toPageableItemList(page.getContent());

        boolean isFirst = (pageRequest.getPageNumber() == 0);
        boolean isLast = (pageRequest.getPageNumber() == page.getTotalPages() - 1);
        long totalElements = filmRepository.count();

        filmDto.setData(data);
        PageableDto sorted = new PageableDto();
        sorted.setSort(pageRequest.getSort());
        sorted.setEmpty(data.isEmpty());
        sorted.setFirst(isFirst);
        sorted.setLast(isLast);
        sorted.setSize(page.getNumberOfElements());
        sorted.setPageNumber(page.getNumber());
        sorted.setTotalElements(totalElements);
        sorted.setTotalPages(page.getTotalPages());
        filmDto.setPageable(sorted);

        return filmDto;
    }

    public PageableFilmItem update(Long id,
                                   String name,
                                   Genre genre,
                                   Integer durationMins,
                                   String company,
                                   String director,
                                   String producers,
                                   String writers,
                                   String cast,
                                   String trailerUrl,
                                   String synopsis,
                                   Integer completionYear,
                                   MultipartFile photo) {
        Film film = getById(id);
        film.setName(name);
        film.setGenre(genre);
        film.setDurationMins(durationMins);
        film.setCompany(company);
        film.setDirector(director);
        film.setProducers(producers);
        film.setWriters(writers);
        film.setCast(cast);
        film.setTrailerUrl(trailerUrl);
        film.setSynopsis(synopsis);
        film.setCompletionYear(completionYear);
        filmRepository.save(film);

        if (photo != null) {
            fileService.updateItemPhoto(film.getId(), ItemType.FILM, photo);
        }

        return FilmMapper.toPageableItem(film);
    }

    public PageableFilmItem updateFilmCollection(Long filmId, String collectionName) {
        Film film = getByIdWithCollections(filmId);
        Collection collection = collectionService.getByName(collectionName);
        if (!film.getCollections().contains(collection)) {
            film.getCollections().add(collection);
            filmRepository.save(film);
        }
        return FilmMapper.toPageableItem(film);
    }

    public PageableFilmItem removeFilmCollection(Long filmId, Long collectionId) {
        Film film = getByIdWithCollections(filmId);
        Collection collection = collectionService.getById(collectionId);
        if (film.getCollections().contains(collection)) {
            film.getCollections().remove(collection);
            filmRepository.save(film);
        }
        return FilmMapper.toPageableItem(film);
    }

    public void delete(Long id) {
        filmRepository.deleteById(id);
    }
}
