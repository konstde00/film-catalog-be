package com.konstde00.filmcatalog.model.dto.collections;

import com.konstde00.filmcatalog.model.dto.common.PageableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PageableCollectionsDto {

    List<PageableCollectionItem> data;

    PageableDto pageable;
}
