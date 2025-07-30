package com.CareGenius.book.Dto;

import com.CareGenius.book.Model.CareNeed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CareNeedDto {

    private Set<CareNeed> careNeedOrSkills;
}
