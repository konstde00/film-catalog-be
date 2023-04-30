package com.konstde00.filmcatalog.model.dto.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SendGridResponseDto {

    Long userId;

    Integer statusCode;

    String body;
}
