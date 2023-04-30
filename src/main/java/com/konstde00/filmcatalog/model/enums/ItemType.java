package com.konstde00.filmcatalog.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {

    FILM("films"),
    COLLECTION("collections");

    private final String s3ItemPrefix;
}
