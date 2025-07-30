package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareGiverDto;
import com.CareGenius.book.Dto.CareGiverRequestDto;
import com.CareGenius.book.Dto.CertificationDto;
import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Model.*;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.CareGiverService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CareGiverServiceImp implements CareGiverService {

    private final CareGiverRepository careGiverRepository;
    private final UserRepository userRepository;
    private final UserServiceImp userServiceImp;
    private final Cloudinary cloudinary;

    @Override
    public CareGiverDto createCareGiver(CareGiverDto careGiverDto) {
        boolean emailDB = userRepository.existsByEmail(careGiverDto.getUserDto().getEmail());
        boolean phoneDB = careGiverRepository.existsByPhoneNumber(careGiverDto.getCareGiverRequestDto().getPhoneNumber());

        if(emailDB && phoneDB ){
            throw new IllegalArgumentException("This CareGiver already exists !");
        }

        User createUser = userServiceImp.map(careGiverDto.getUserDto());
        createUser.setRole(Role.GIVER);
        userRepository.save(createUser);

        CareGiver careGiver = mapToCareGiver(careGiverDto.getCareGiverRequestDto());
        careGiver.setUser(createUser);

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

        Schedule schedule = careGiverDto.getSchedule();
        checkValidSchedule(schedule);

        schedule.setCareGiver(careGiver);
        careGiver.setSchedule(schedule);

        careGiverRepository.save(careGiver);
        return careGiverDto;
    }

    @Override
    public String linkImageToGiver(String giverUid, MultipartFile file) {
        CareGiver careGiverDB = careGiverRepository.findById(giverUid).orElseThrow(()-> new IllegalArgumentException("No givers foudn"));
        String linkImageUrl = uploadFile(file);
        careGiverDB.setImageUrl(linkImageUrl);
        careGiverRepository.save(careGiverDB);
        return "Save image successfully";
    }

    @Override
    public CareGiverDto getByUid(String uid) {
        CareGiver careGiverDB = careGiverRepository.findById(uid).orElseThrow(()-> new IllegalArgumentException("Can not find giver with this uid"));
        return mapToCareGiverDto(careGiverDB);
    }

    private CareGiverDto mapToCareGiverDto(CareGiver careGiverDB) {
        return new CareGiverDto(
                new UserDto(careGiverDB.getUser().getFullName(),
                        "",
                        "",
                        careGiverDB.getUser().getEmail(),
                        "",
                        careGiverDB.getUser().getGender()
                ),
                new CareGiverRequestDto(careGiverDB.getDob(),
                        careGiverDB.getPhoneNumber(),
                        careGiverDB.getYearExperience(),
                        careGiverDB.getFee(),
                        careGiverDB.getBio()
                        ),
                mapToCareNeed(careGiverDB.getSkills()),
                mapToCareGiverCert(careGiverDB.getCertifications()),
                careGiverDB.getSchedule()
        );
    }

    private List<CertificationDto> mapToCareGiverCert(Set<Certification> certifications) {
        List<CertificationDto> certificationDtos = certifications.stream().map((cert)->
                new CertificationDto(cert.getCertificateName(), cert.getIssueDate(), cert.getExpirationDate(), cert.getIssuer())).collect(Collectors.toList());
        return certificationDtos;
    }

    private List<CareNeed> mapToCareNeed(Set<CareGiverSkill> skills) {
        List<CareNeed> careNeeds = skills.stream().map((skill)->
                skill.getSkillName()).collect(Collectors.toList());
        return careNeeds;
    }

    @Override
    public List<CareGiverDto> getAll() {
        return careGiverRepository.findAll().stream().map(this::mapToCareGiverDto).collect(Collectors.toList());
    }

    public void checkValidSchedule(Schedule schedule){
        if(schedule.getDayOfWeeks() == null || schedule.getDayOfWeeks().isEmpty()){
            throw new IllegalArgumentException("Schedule must have at least one day");
        }
        if(schedule.getStartTime() == null || schedule.getEndTime() == null){
            throw new IllegalArgumentException("StartTime and EndTime must not be null");
        }
        if(schedule.getStartTime().isAfter(schedule.getEndTime())){
            throw new IllegalArgumentException("StartTime must be before EndTime");
        }
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

    private String uploadFile(MultipartFile file) {
        try{
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        }catch (IOException e){
            throw new RuntimeException("Lỗi khi upload file: " + e.getMessage());
        }
    }





}
