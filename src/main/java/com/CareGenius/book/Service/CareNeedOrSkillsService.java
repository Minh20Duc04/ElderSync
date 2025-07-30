package com.CareGenius.book.Service;

import com.CareGenius.book.Dto.CareNeedDto;

public interface CareNeedOrSkillsService {

    CareNeedDto updateCareNeedOrSkills(String seekerOrGiverUid, CareNeedDto careNeedDto);

}
