package com.hospital.common.exception;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;
    private int status;
    private LocalDateTime time;
}