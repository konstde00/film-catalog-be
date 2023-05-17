package com.konstde00.filmcatalog.mapper;

import com.konstde00.filmcatalog.model.dto.collections.CreateCollectionDto;
import com.konstde00.filmcatalog.model.dto.collections.PageableCollectionItem;
import com.konstde00.filmcatalog.model.dto.films.PageableFilmItem;
import com.konstde00.filmcatalog.model.entity.Collection;
import com.konstde00.filmcatalog.model.entity.Film;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CollectionMapper {

    public static List<PageableCollectionItem> toPageableItemList(List<Collection> collections) {
        return collections.stream()
                .map(CollectionMapper::toPageableItem)
                .collect(Collectors.toList());
    }

    public static PageableCollectionItem toPageableItem(Collection collection) {
        return new PageableCollectionItem()
                .builder()
                .id(collection.getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .photoUrl("https://filmcatalog.s3.amazonaws.com/collections/" + collection.getId() + "/photo")
                .lastUpdate(Objects.requireNonNullElse(collection.getUpdatedAt(),
                        collection.getCreatedAt()))
                .build();
    }

    public static PageableCollectionItem toPageableItemWithFilms(Collection collection) {
        return new PageableCollectionItem()
                .builder()
                .id(collection.getId())
                .name(collection.getName())
                .description(collection.getDescription())
                .films(FilmMapper.toPageableItemList(collection.getFilms()))
                .photoUrl("https://filmcatalog.s3.amazonaws.com/collections/" + collection.getId() + "/photo")
                .lastUpdate(Objects.requireNonNullElse(collection.getUpdatedAt(),
                        collection.getCreatedAt()))
                .build();
    }

    public static Collection toEntity(CreateCollectionDto createCollectionDto) {
        return new Collection()
                .builder()
                .name(createCollectionDto.getName())
                .build();
    }
}
