package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CertificationDto;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.Certification;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CertificationRepository;
import com.CareGenius.book.Service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CertificationImp implements CertificationService {

    private final CareGiverRepository careGiverRepository;
    private final CertificationRepository certificationRepository;

    @Override //dung PathVariable va RequestBody
    public Set<CertificationDto> createOrUpdateGiverCert(String giverUid, Set<CertificationDto> certificationDto) {
        certificationDto.forEach(cert ->
                checkValidDate(cert.getIssueDate(), cert.getExpirationDate()));

        Optional<CareGiver> careGiverDB = careGiverRepository.findById(giverUid);

        Set<Certification> certifications = certificationDto.stream().map(cert -> {
            Certification.CertificationBuilder builder = Certification.builder()
                    .certificateName(cert.getCertificateName())
                    .issuer(cert.getIssuer())
                    .issueDate(cert.getIssueDate())
                    .expirationDate(cert.getExpirationDate());

            careGiverDB.ifPresent(builder::careGiver);

            return builder.build();
        }).collect(Collectors.toSet());

        if(careGiverDB.isPresent()){
            careGiverDB.get().setCertifications(certifications);
        }

        certificationRepository.saveAll(certifications);

        return certificationDto;
    }

    private void checkValidDate(LocalDate issueDate, LocalDate expirationDate){
        if(issueDate == null || expirationDate == null){
            throw new IllegalArgumentException("issueDate and expirationDate must not be null");
        }
        if(issueDate.isAfter(expirationDate)){
            throw new IllegalArgumentException("issueDate must be before expirationDate");
        }
    }


}

