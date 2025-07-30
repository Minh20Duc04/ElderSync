package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.CertificationDto;

import java.util.Set;

public interface CertificationService {

    Set<CertificationDto> createOrUpdateGiverCert(String giverUid, Set<CertificationDto> certificationDto);



}
