package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Config.AppConfig;
import com.CareGenius.book.Model.AIRecommendation;
import com.CareGenius.book.Model.CareGiver;
import com.CareGenius.book.Model.CareNeed;
import com.CareGenius.book.Model.CareSeeker;
import com.CareGenius.book.Repository.AIRecommendationRepository;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CareGiverSkillRepository;
import com.CareGenius.book.Repository.CareSeekerRepository;
import com.CareGenius.book.Service.AIRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class AIRecommendationServiceImp implements AIRecommendationService {

    private final AIRecommendationRepository aiRecommendationRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareSeekerRepository careSeekerRepository;
    private final CareGiverSkillRepository careGiverSkillRepository;
    private final AppConfig appConfig;


    @Override
    @Transactional
    public List<CareGiver> AIRecommendationMatching(CareSeeker careSeeker) {
        List<CareGiver> giversDB = careGiverRepository.findAll();
        List<CareGiver> giversSuitable = new ArrayList<>();

        Set<String> seekerCareNeedDes = careSeeker.getCareNeedsDescription().stream().map((s1)->
                s1.name()).collect(Collectors.toSet());

        Set<String> seekerHealthCon = careSeeker.getHealthConditions().stream().map((s2)->
                s2.name()).collect(Collectors.toSet());

        //bat dau loc het giver tu database, neu loc ra kqua true thi +20 point
            giversDB.forEach((giverDB) ->{
            int pointEarned = 0;

            Set<String> giverSkills = giverDB.getSkills().stream().map((t1)->
                    t1.getSkillName().name()).collect(Collectors.toSet());

            Set<String> giverCerts = giverDB.getCertifications().stream().map((t2)->
                    t2.getCertificateName()).collect(Collectors.toSet());

            String giverGender = giverDB.getUser().getGender().name();

            String giverAddress = giverDB.getUser().getAddress();

            if(checkSuitable(seekerCareNeedDes, giverSkills, "skill")){
                pointEarned += 20;
            }
            if(checkSuitable(seekerHealthCon, giverCerts, "health")){
                pointEarned += 20;
            }
            if(checkSuitable(careSeeker.getPreferredGiverGender(), giverGender, "gender")){
                pointEarned += 20;
            }
            if(checkSuitable(careSeeker.getUser().getAddress(), giverAddress, "address")){
                pointEarned += 20;
            }
            if(pointEarned >= 0){
                giversSuitable.add(giverDB);
                aiRecommendationRepository.save(
                        AIRecommendation.builder()
                                .matchPoint(pointEarned)
                                .careGiver(giverDB)
                                .careSeeker(careSeeker)
                                .build());
            }
        });
            return giversSuitable;
    }


    private boolean checkSuitable(Object seekerData, Object giverData, String type){
        String prompt = buildPrompt(seekerData, giverData, type);

        String result = appConfig.getAiMatchingResult(prompt);

        return result.contains("yes") || result.contains("phù hợp") || result.contains("match"); //AI no loc ve 1 doan text, kiem tra xem doan text do co "dong y" giua cac truong cua seeker va giver khong
    }

    //tao prompt xong gui len AI loc
    private String buildPrompt(Object seekerData, Object giverData, String type){
        String promptType = null;

        switch (type){
            case "skill":
                promptType = "CareSeeker cần: " + seekerData + ". CareGiver có kỹ năng: " + giverData +
                        ". Những kỹ năng này có phù hợp với nhu cầu không? Trả lời yes hoặc no.";
                break;
            case "health":
                promptType = "CareSeeker có tình trạng sức khỏe: " + seekerData +
                        ". CareGiver có chứng chỉ/kinh nghiệm: " + giverData +
                        ". Họ có phù hợp để chăm sóc không? Trả lời yes hoặc no.";
                break;
            case "gender":
                promptType = "CareSeeker muốn người chăm sóc giới tính: " + seekerData +
                        ". CareGiver là: " + giverData +
                        ". Có phù hợp không? Trả lời yes hoặc no.";
                break;
            case  "address":
                promptType = "CareSeeker sống tại: " + seekerData +
                        ". CareGiver sống tại: " + giverData +
                        ". Hai địa điểm này có cùng thành phố không? Trả lời yes hoặc no.";
                break;
            default:
                promptType = "Hãy so sánh " + seekerData + " và " + giverData + ". Chúng có phù hợp không?";
        }
        return promptType;
    }


    /*
        seekerDB = seekerRepios.findByUser(userDB) //Authentication lúc đăng nhập
        List<Giver> givers = giverRepos.findAll();

        forEach Giver gv : givers {
            int point = 0;
            if(checkSuitable(gv.skills, seekerDB.careNeedsDescription)
            {
                point += 20;
            } if(checkSuitable(gv.certificateName, seekerDB.healthConditions)
            {
                point += 20;
            } if(checkSuitable(gv.getUser().getGender(), seekerDB.preferredGiverGender)
            {
                point += 20;
            } if(checkSuitable(gv.getUser().getAddress(), seekerDB.getUser().getAddress())
            {
                point += 20;
            }
            if(point < 60){
                givers.remove(gv);
            }
        }

        nếu điểm > 60 lọc theo ycau và gửi

        private boolean checkSuitable(Object seekerData, Object giverData)
        {
            gọi kiểm tra bằng AI
        }
     */

}
