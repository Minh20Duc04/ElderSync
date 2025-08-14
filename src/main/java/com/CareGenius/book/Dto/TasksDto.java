package com.CareGenius.book.Dto;

import com.CareGenius.book.Model.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TasksDto {

    private Long id;

    private String taskName;

    private Type type;

    private Long bookingId;

}
