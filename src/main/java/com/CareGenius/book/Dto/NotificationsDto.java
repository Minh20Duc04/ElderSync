package com.CareGenius.book.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NotificationsDto {

    private String message;

    private String type;

    private LocalDate createdAt;

    private List<?> payload;
}
