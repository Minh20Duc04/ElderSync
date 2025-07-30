package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.CareGiverDto;
import com.CareGenius.book.Dto.CareGiverResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CareGiverService {
    CareGiverDto createCareGiver(CareGiverDto careGiverDto);

    String linkImageToGiver(String giverUid, MultipartFile file);

    CareGiverResponseDto getByUid(String uid);

    List<CareGiverResponseDto> getAll();
}
