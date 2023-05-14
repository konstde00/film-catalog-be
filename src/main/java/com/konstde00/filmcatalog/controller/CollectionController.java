package com.konstde00.filmcatalog.controller;

import com.konstde00.filmcatalog.mapper.CollectionMapper;
import com.konstde00.filmcatalog.model.dto.collections.CreateCollectionDto;
import com.konstde00.filmcatalog.model.dto.collections.PageableCollectionItem;
import com.konstde00.filmcatalog.model.dto.collections.PageableCollectionsDto;
import com.konstde00.filmcatalog.model.entity.User;
import com.konstde00.filmcatalog.service.CollectionService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collections")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollectionController {

    UserService userService;
    CollectionService collectionService;

    @GetMapping("/v1")
    @Operation(summary = "Get list of collections")
    public ResponseEntity<PageableCollectionsDto> getCollections(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                                 @RequestParam(value = "itemsOnPage", required = false, defaultValue = "10") Integer itemsOnPage,
                                                                 HttpServletRequest request) {

        var collections = collectionService.getAll(pageNumber, itemsOnPage);

        return ResponseEntity.ok(collections);
    }

    @GetMapping("/v1/{id}")
    @Operation(summary = "Get collection by id")
    public ResponseEntity<PageableCollectionItem> getCollections(@PathVariable Long id,
                                                                 @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                                                 @RequestParam(value = "itemsOnPage", required = false, defaultValue = "10") Integer itemsOnPage) {

        var collection = CollectionMapper.toPageableItemWithFilms(collectionService.getByIdWithFilms(id));

        return ResponseEntity.ok(collection);
    }

    @PostMapping("/v1")
    @Operation(summary = "Create collection")
    public ResponseEntity<PageableCollectionItem> createCollection(@RequestParam String name,
                                                                   @RequestParam String description,
                                                                   MultipartFile photo,
                                                                   HttpServletRequest httpServletRequest) {

        User currentUser = userService.getCurrentUser(httpServletRequest);

        var collection = collectionService.create(name, description, photo, currentUser);

        return ResponseEntity.ok(collection);
    }

    @PatchMapping("/v1")
    @Operation(summary = "Update collection")
    public ResponseEntity<PageableCollectionItem> updateCollection(@RequestParam Long collectionId,
                                                                   @RequestParam String name,
                                                                   @RequestParam String description,
                                                                   MultipartFile photo) {

        var collection = collectionService.update(collectionId, name, description, photo);

        return ResponseEntity.ok(collection);
    }

    @DeleteMapping("/v1")
    @Operation(summary = "Delete collection")
    public ResponseEntity<?> delete(@RequestParam Long id) {

        collectionService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
