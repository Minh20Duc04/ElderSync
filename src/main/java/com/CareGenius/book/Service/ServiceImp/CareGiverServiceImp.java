package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareGiverDto;
import com.CareGenius.book.Dto.CareGiverRequestDto;
import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CareGiverSkillRepository;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.CareGiverService;
import com.CareGenius.book.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CareGiverServiceImp implements CareGiverService {

    private final CareGiverRepository careGiverRepository;
    private final UserRepository userRepository;
    private final UserServiceImp userServiceImp;
    private final CareGiverSkillRepository careGiverSkillRepository;

    @Override
    public CareGiver createCareGiver(CareGiverDto careGiverDto) {
        boolean emailDB = userRepository.existsByEmail(careGiverDto.getUserDto().getEmail());
        boolean phoneDB = careGiverRepository.existsByPhoneNumber(careGiverDto.getCareGiverRequestDto().getPhoneNumber());

        if(emailDB && phoneDB ){
            throw new IllegalArgumentException("This CareGiver already exists !");
        }

        User createUser = userServiceImp.map(careGiverDto.getUserDto());
        createUser.setRole(Role.GIVER);
        userRepository.save(createUser);

        CareGiver careGiver = mapToCareGiver(careGiverDto.getCareGiverRequestDto());

        Set<CareGiverSkill> skills = careGiverDto.getSkills().stream()
                .map((skill)->
                        CareGiverSkill.builder()
                                .careGiver(careGiver)
                                .skillName(skill)
                                .build())
                .collect(Collectors.toSet());

        careGiver.setSkills(skills);

        Set<Certification> certifications = careGiverDto.getCertifications().stream()
                .map((cert)->
                        Certification.builder()
                                .certificateName(cert.getCertificateName())
                                .careGiver(careGiver)
                                .issuer(cert.getIssuer())
                                .issueDate(cert.getIssueDate())
                                .expirationDate(cert.getExpirationDate())
                                .build())
                .collect(Collectors.toSet());

        careGiver.setCertifications(certifications);

        careGiver.setSchedule(careGiverDto.getSchedule());

        return careGiverRepository.save(careGiver);
    }

    private CareGiver mapToCareGiver(CareGiverRequestDto careGiverRequestDto) {
        return CareGiver.builder()
                .dob(careGiverRequestDto.getDob())
                .phoneNumber(careGiverRequestDto.getPhoneNumber())
                .yearExperience(careGiverRequestDto.getYearExperience())
                .fee(careGiverRequestDto.getFee())
                .bio(careGiverRequestDto.getBio())
                .build();
    }

    private String uploadFile(String imageUrl) {

    }


}
