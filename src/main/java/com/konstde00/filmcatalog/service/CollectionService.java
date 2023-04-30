package com.konstde00.filmcatalog.service;

import com.konstde00.filmcatalog.mapper.CollectionMapper;
import com.konstde00.filmcatalog.mapper.FilmMapper;
import com.konstde00.filmcatalog.model.dto.collections.CreateCollectionDto;
import com.konstde00.filmcatalog.model.dto.collections.PageableCollectionItem;
import com.konstde00.filmcatalog.model.dto.collections.PageableCollectionsDto;
import com.konstde00.filmcatalog.model.dto.common.PageableDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmDto;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmItem;
import com.konstde00.filmcatalog.model.entity.Collection;
import com.konstde00.filmcatalog.model.entity.Film;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.model.enums.ItemType;
import com.konstde00.filmcatalog.model.exception.NotValidException;
import com.konstde00.filmcatalog.repository.CollectionRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollectionService {

    FileService fileService;
    CollectionRepository collectionRepository;

    public void checkExistsByName(String name) {

        if (collectionRepository.existsByName(name)) {
            throw new NotValidException("Collection with name '" + name + "' already exists");
        }
    }

    public Collection getById(Long id) {
        return collectionRepository.findById(id).orElseThrow(() ->
                new NotValidException("Collection with id " + id + " not found"));
    }

    public Collection getByIdWithFilms(Long id) {
        return collectionRepository.findByIdWithFilms(id).orElseThrow(() ->
                new NotValidException("Collection with id " + id + " not found"));
    }

    public Collection getByName(String name) {

        return collectionRepository.findByName(name).orElseThrow(() ->
                new NotValidException("Collection with name " + name + " not found"));
    }

    public PageableCollectionsDto getAll(Integer pageNumber, Integer itemsOnPage) {

        Pageable pageRequest = PageRequest.of(pageNumber, itemsOnPage);

        PageableCollectionsDto collectionsDto = new PageableCollectionsDto();
        Page<Collection> page = collectionRepository.findAll(pageRequest);
        List<PageableCollectionItem> data = CollectionMapper
                .toPageableItemList(page.getContent());

        boolean isFirst = (pageRequest.getPageNumber() == 0);
        boolean isLast = (pageRequest.getPageNumber() == page.getTotalPages() - 1);
        long totalElements = collectionRepository.count();

        collectionsDto.setData(data);
        PageableDto sorted = new PageableDto();
        sorted.setSort(pageRequest.getSort());
        sorted.setEmpty(data.isEmpty());
        sorted.setFirst(isFirst);
        sorted.setLast(isLast);
        sorted.setSize(page.getNumberOfElements());
        sorted.setPageNumber(page.getNumber());
        sorted.setTotalElements(totalElements);
        sorted.setTotalPages(page.getTotalPages());
        collectionsDto.setPageable(sorted);

        return collectionsDto;
    }

    public PageableCollectionItem create(String name, String description, MultipartFile photo, User currentUser) {

        checkExistsByName(name);

        Collection collection = Collection
                .builder()
                .name(name)
                .description(description)
                .build();

        collection.setCreator(currentUser);
        Collection savedCollection = collectionRepository.save(collection);

        if (photo == null) {
            fileService.uploadDefaultImage(savedCollection.getId(), ItemType.COLLECTION);
        } else {
            fileService.updateItemPhoto(savedCollection.getId(), ItemType.COLLECTION, photo);
        }

        return CollectionMapper.toPageableItem(savedCollection);
    }

    public PageableCollectionItem update(Long collectionId, String name, String description, MultipartFile photo) {
        Collection collection = getById(collectionId);
        collection.setName(name);
        collection.setDescription(description);

        if (photo != null) {
            fileService.updateItemPhoto(collectionId, ItemType.COLLECTION, photo);
        }

        Collection savedCollection = collectionRepository.save(collection);
        return CollectionMapper.toPageableItem(savedCollection);
    }

    public void delete(Long id) {
        collectionRepository.deleteById(id);
    }
}
