package com.CareGenius.book.Controller;

import com.CareGenius.book.Dto.TasksDto;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor

public class TaskController {

    private final TasksService tasksService;

    @PostMapping("/create")
    public ResponseEntity<TasksDto> createTask(Authentication auth, @RequestBody TasksDto tasksDto) {
        User userDB =(User) auth.getPrincipal();
        return ResponseEntity.ok(tasksService.createTask(userDB, tasksDto));
    }

    @GetMapping("/getById/{taskId}")
    public ResponseEntity<TasksDto> getTaskById(@PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok(tasksService.getTaskById(taskId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TasksDto>> getAllTasks(Authentication auth) { //cho admin
        User userDB =(User) auth.getPrincipal();
        return ResponseEntity.ok(tasksService.getAllTasks(userDB));
    }

    @GetMapping("/getAllByBooking/{bookingId}")
    public ResponseEntity<List<TasksDto>> getAllTasksByBooking(Authentication auth, @PathVariable("bookingId") Long bookingId) {
        User userDB =(User) auth.getPrincipal();
        return ResponseEntity.ok(tasksService.getAllTasksByBooking(userDB, bookingId));
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<TasksDto> updateTaskById(Authentication auth, @PathVariable Long taskId, @RequestBody TasksDto tasksDto) {
        User userDB =(User) auth.getPrincipal();
        return ResponseEntity.ok(tasksService.updateTask(userDB, taskId, tasksDto));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTaskById(Authentication auth, @PathVariable Long taskId) {
        User userDB =(User) auth.getPrincipal();
        return ResponseEntity.ok(tasksService.deleteTask(userDB,taskId));
    }



}
