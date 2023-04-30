package com.konstde00.filmcatalog.model.dto.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeviceTokenDto {

    String deviceToken;
}
