package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.Schedule;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.ScheduleRepository;
import com.CareGenius.book.Service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleImp implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareGiverServiceImp careGiverServiceImp;

    @Override
    public Schedule creatOrUpdateGiverSchedule(String careGiverUid, Schedule schedule) {
        careGiverServiceImp.checkValidSchedule(schedule);

        Optional<CareGiver> careGiverDB = careGiverRepository.findById(careGiverUid);

        if(careGiverDB.isPresent()){
            careGiverDB.get().setSchedule(schedule);
            schedule.setCareGiver(careGiverDB.get());
        }
        
        return scheduleRepository.save(schedule);

    }



}
