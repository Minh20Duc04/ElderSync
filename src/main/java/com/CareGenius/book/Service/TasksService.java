package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.TasksDto;
import com.CareGenius.book.Model.User;

import java.util.List;

public interface TasksService {

    TasksDto createTask(User userDB, TasksDto tasksDto);

    TasksDto getTaskById(Long taskId);

    List<TasksDto> getAllTasks(User userDB);

    TasksDto updateTask(User userDB, Long taskId, TasksDto tasksDtodto);

    String deleteTask(User userDB, Long taskId);

    List<TasksDto> getAllTasksByBooking(User userDB, Long bookingId);
}
