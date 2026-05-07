package com.hospital.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@Accessors(chain = true)
public abstract class BaseRequestDto {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
