package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.NotificationsDto;
import com.CareGenius.book.Model.User;

import java.util.List;

public interface NotificationService {

    List<NotificationsDto> getNotification(User userDB);

    void requestEmergency(User userDB);
}
