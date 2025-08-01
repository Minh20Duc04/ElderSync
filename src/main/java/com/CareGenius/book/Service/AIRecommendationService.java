package com.CareGenius.book.Service;

import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.CareSeeker;

import java.util.List;

public interface AIRecommendationService {

    List<CareGiver> AIRecommendationMatching(CareSeeker careSeeker);
}
