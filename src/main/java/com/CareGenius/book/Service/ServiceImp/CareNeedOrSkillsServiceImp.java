package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.CareNeedDto;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.CareGiverSkill;
import com.CareGenius.book.Model.CareNeed;
import com.CareGenius.book.Model.CareSeeker;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CareGiverSkillRepository;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Service.CareNeedOrSkillsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareNeedOrSkillsServiceImp implements CareNeedOrSkillsService {

    private final CareGiverRepository careGiverRepository;
    private final CareGiverSkillRepository careGiverSkillRepository;
    private final CareSeekerRepository careSeekerRepository;


    @Override //cap nhat dua tren uid cua seeker hoac giver
    public CareNeedDto updateCareNeedOrSkills(String seekerOrGiverUid, CareNeedDto careNeedDto) {
        Optional<CareGiver> careGiverDB = careGiverRepository.findById(seekerOrGiverUid);

        Optional<CareSeeker> careSeekerDB = careSeekerRepository.findById(seekerOrGiverUid);

        if(careGiverDB.isPresent()){
            careGiverSkillRepository.deleteAll(careGiverDB.get().getSkills());
            Set<CareGiverSkill> careGiverSkills = careNeedDto.getCareNeedOrSkills().stream().map(
                    (skills)-> CareGiverSkill.builder()
                            .careGiver(careGiverDB.get())
                            .skillName(skills)
                            .build()
            ).collect(Collectors.toSet());
            careGiverDB.get().setSkills(careGiverSkills);
            careGiverRepository.save(careGiverDB.get());

        }else if(careSeekerDB.isPresent()){
            careSeekerDB.get().setCareNeedsDescription(careNeedDto.getCareNeedOrSkills());
            careSeekerRepository.save(careSeekerDB.get());
        }
        else {
            throw new RuntimeException("Not found CareGiver and CareSeeker");
        }
        return careNeedDto;
    }





}
