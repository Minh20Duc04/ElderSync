package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.CareGiverDto;
import com.CareGenius.book.Model.CareGiver;

public interface CareGiverService {
    CareGiver createCareGiver(CareGiverDto careGiverDto);
}
