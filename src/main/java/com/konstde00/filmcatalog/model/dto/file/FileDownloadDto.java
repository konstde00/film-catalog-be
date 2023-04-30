package com.konstde00.filmcatalog.model.dto.file;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(level = PRIVATE)
public class FileDownloadDto {

    String fileName;

    String contentType;

    long contentLength;

    InputStreamResource data;
}
