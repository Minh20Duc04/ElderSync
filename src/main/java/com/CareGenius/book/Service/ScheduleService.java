package com.CareGenius.book.Service;

import com.CareGenius.book.Model.Schedule;

public interface ScheduleService {

    Schedule creatOrUpdateGiverSchedule(String careGiverUid ,Schedule schedule);
}
