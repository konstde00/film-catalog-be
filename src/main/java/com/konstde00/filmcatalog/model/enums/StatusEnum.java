package com.konstde00.filmcatalog.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    GREAT(1L),
    OK(2L),
    BAD(3L);

    private final Long id;
}
