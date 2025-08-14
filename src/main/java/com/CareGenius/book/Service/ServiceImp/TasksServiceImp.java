package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.TasksDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.BookingRepository;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Repository.TasksRepository;
import com.CareGenius.book.Service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class TasksServiceImp implements TasksService {

    private final TasksRepository tasksRepository;
    private final BookingRepository bookingRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareSeekerRepository careSeekerRepository;

    @Override
    public TasksDto createTask(User userDB, TasksDto tasksDto) {
        CareGiver careGiverDB = careGiverRepository.findByUser(userDB);
        Booking bookingDB = bookingRepository.findById(tasksDto.getBookingId()).orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if(!bookingDB.getCareGiver().getUid().equals(careGiverDB.getUid())){
            throw new RuntimeException("You don't have permission to create tasks on this booking !");
        }

        Tasks task = Tasks.builder()
                .taskName(tasksDto.getTaskName())
                .type(Type.NEW_MESSAGE)
                .booking(bookingDB)
                .build();

        tasksRepository.save(task);
        return mapToDto(task);
    }

    @Override
    public TasksDto getTaskById(Long taskId) {
        return tasksRepository.findById(taskId).map(this::mapToDto).orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    @Override
    public List<TasksDto> getAllTasks(User userDB) {
        List<Tasks> tasksDB = tasksRepository.findAll();
        return tasksDB.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TasksDto updateTask(User userDB, Long taskId, TasksDto tasksDto) {
        CareGiver careGiverDB = careGiverRepository.findByUser(userDB);
        Tasks taskDB = tasksRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if(!taskDB.getBooking().getCareGiver().getUid().equals(careGiverDB.getUid())){
            throw new RuntimeException("You don't have permission to create tasks on this booking !");
        }

        if (tasksDto.getTaskName() != null){
            taskDB.setTaskName(tasksDto.getTaskName());
        }
        if (tasksDto.getType() != null){
            taskDB.setType(tasksDto.getType());
        }
        if (tasksDto.getBookingId() != null){ //ví dụ giver đặt sai task, muốn đặt lại task vào đúng booknig thì
            Booking booking = bookingRepository.findById(tasksDto.getBookingId()).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
            taskDB.setBooking(booking);
        }

        tasksRepository.save(taskDB);
        return mapToDto(taskDB);
    }

    @Override
    public String deleteTask(User userDB, Long taskId) {
        CareGiver careGiverDB = careGiverRepository.findByUser(userDB);
        Tasks tasksDB = tasksRepository.findById(taskId).orElseThrow(()-> new IllegalArgumentException("Task not found"));

        if (!tasksDB.getBooking().getCareGiver().getUid().equals(careGiverDB.getUid())){
            throw new RuntimeException("You don't have permission to delete this task");
        }

        tasksRepository.deleteById(taskId);
        return "Deleted task successfully";
    }

    @Override
    public List<TasksDto> getAllTasksByBooking(User userDB, Long bookingId) {
        Booking bookingDB = bookingRepository.findById(bookingId).orElseThrow(()-> new IllegalArgumentException("Book not found"));

        CareGiver careGiverDB = careGiverRepository.findByUser(userDB);
        CareSeeker careSeekerDB = careSeekerRepository.findByUser(userDB);

        // chỉ có seeker và giver của booking đó mới xem đc tasks đó
        // userDB.getGiver == bookingDB.getGiver or userDB.getSeeker == bookingDB.getSeeker  -> ListTask = taskRepos.
        List<TasksDto> tasksDtos = new LinkedList<>();

        //if(bookingDB.getCareSeeker().getUid().equals(careSeekerDB.getUid()) || bookingDB.getCareGiver().getUid().equals(careGiverDB.getUid())){
        //cái if trên sai vì nếu 1 th là null thì nó quăng lỗi luôn, không kiểm tra phía sau ||

        if(((careSeekerDB != null && bookingDB.getCareSeeker().getUid().equals(careSeekerDB.getUid())) || (careGiverDB != null && bookingDB.getCareGiver().getUid().equals(careGiverDB.getUid())))){
        List<Tasks> tasksDB = tasksRepository.findByBooking(bookingDB);
            tasksDtos = tasksDB.stream().map(this::mapToDto).collect(Collectors.toList());
        } else {
            throw new RuntimeException("You don't have permission to view tasks for this booking !");
        }

        if(tasksDtos.isEmpty()){
            throw new IllegalArgumentException("Maybe this booking has no tasks !");
        }
        return tasksDtos;
    }

    private TasksDto mapToDto(Tasks task) {
        return new TasksDto(
            task.getId(),
            task.getTaskName(),
            task.getType(),
            task.getBooking().getId()
        );
    }

}
