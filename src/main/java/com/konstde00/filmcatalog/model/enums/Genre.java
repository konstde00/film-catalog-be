package com.konstde00.filmcatalog.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genre {

    ACTION("Action"),
    ADVENTURE("Adventure"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    FANTASY("Fantasy"),
    HISTORICAL("Historical"),
    HORROR("Horror"),
    ROMANCE("Romance"),
    THRILLER("Thriller"),
    WESTERN("Western");

    private String value;
}
