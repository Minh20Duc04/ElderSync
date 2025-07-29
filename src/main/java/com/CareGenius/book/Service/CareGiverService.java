package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.CareGiverDto;
import org.springframework.web.multipart.MultipartFile;

public interface CareGiverService {
    CareGiverDto createCareGiver(CareGiverDto careGiverDto);

    String linkImageToGiver(String giverUid, MultipartFile file);
}
