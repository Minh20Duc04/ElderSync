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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class AIRecommendationServiceImp implements AIRecommendationService {

    private final AIRecommendationRepository aiRecommendationRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareSeekerRepository careSeekerRepository;
    private final CareGiverSkillRepository careGiverSkillRepository;
    private final AppConfig appConfig;

    private Logger logging = LoggerFactory.getLogger(AIRecommendationServiceImp.class);

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    @Transactional
    public List<CareGiver> AIRecommendationMatching(CareSeeker careSeeker) {
        List<CareGiver> giversDB = careGiverRepository.findAll();
        List<CareGiver> giversSuitable = new ArrayList<>();

        Set<String> seekerCareNeedDes = careSeeker.getCareNeedsDescription().stream()
                .map(Enum::name).collect(Collectors.toSet());

        Set<String> seekerHealthCon = careSeeker.getHealthConditions().stream()
                .map(Enum::name).collect(Collectors.toSet());

        String seekerGender = careSeeker.getPreferredGiverGender().name();
        String seekerAddress = careSeeker.getUser().getAddress();

        giversDB.forEach((giverDB) -> {
            Set<String> giverSkills = giverDB.getSkills().stream()
                    .map(t1 -> t1.getSkillName().name()).collect(Collectors.toSet());

            Set<String> giverCerts = giverDB.getCertifications().stream()
                    .map(t2 -> t2.getCertificateName()).collect(Collectors.toSet());

            String giverGender = giverDB.getUser().getGender().name();
            String giverAddress = giverDB.getUser().getAddress();

            int pointEarned = checkAllCriteria(seekerCareNeedDes, seekerHealthCon, seekerGender, seekerAddress,
                    giverSkills, giverCerts, giverGender, giverAddress);

            logging.info("The points found for giver {}: {}", giverDB.getUser().getFullName(), pointEarned);

            if (pointEarned >= 40) {
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

    private int checkAllCriteria(Object seekerSkills, Object seekerHealth, Object seekerGender, Object seekerAddress,
                                 Object giverSkills, Object giverCerts, Object giverGender, Object giverAddress) {

        String prompt = "Bạn là hệ thống matching. Hãy so sánh CareSeeker và CareGiver theo 4 tiêu chí: skill, health, gender, address.\n"
                + "Chỉ trả về JSON thuần túy theo format sau, không thêm chữ nào khác:\n"
                + "{\n"
                + "  \"skillMatch\": true/false,\n"
                + "  \"healthMatch\": true/false,\n"
                + "  \"genderMatch\": true/false,\n"
                + "  \"addressMatch\": true/false\n"
                + "}\n\n"
                + "CareSeeker cần: " + seekerSkills
                + ", tình trạng sức khỏe: " + seekerHealth
                + ", muốn CareGiver giới tính: " + seekerGender
                + ", sống tại: " + seekerAddress + ".\n\n"
                + "CareGiver có kỹ năng: " + giverSkills
                + ", chứng chỉ: " + giverCerts
                + ", giới tính: " + giverGender
                + ", sống tại: " + giverAddress + ".";

        String result = appConfig.getAiMatchingResult(prompt);
        logging.info("AI raw response: {}", result);

        String cleaned = extractJson(result);

        int points = 0;
        try {
            JsonNode root = objectMapper.readTree(cleaned);

            if (root.path("skillMatch").asBoolean(false)) points += 20;
            if (root.path("healthMatch").asBoolean(false)) points += 20;
            if (root.path("genderMatch").asBoolean(false)) points += 20;
            if (root.path("addressMatch").asBoolean(false)) points += 20;

        } catch (Exception e) {
            logging.error("Error parsing AI response: {}", e.getMessage());
        }

        return points;
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }


    //Code ban đầu bị lỗi vì model AI ko pro nên ko nhận quá gửi quá nhiều request: org.springframework.web.client.HttpClientErrorException$TooManyRequests: 429 Too Many Requests on POST request for "https://api.groq.com/openai/v1/chat/completions": "{"error":{"message":"Rate limit reached for model `llama3-8b-8192` in organization `org_01jyxmwesjf958a51abtgk7s42` service tier `on_demand` on requests per minute (RPM): Limit 30, Used 30, Requested 1. Please try again in 1.724s. Need more tokens? Upgrade to Dev Tier today at https://console.groq.com/settings/billing","type":"requests","code":"rate_limit_exceeded"}}<EOL>"
//    @Override
//    @Transactional
//    public List<CareGiver> AIRecommendationMatching(CareSeeker careSeeker) {
//
//        List<CareGiver> giversDB = careGiverRepository.findAll();
//        List<CareGiver> giversSuitable = new ArrayList<>();
//
//        Set<String> seekerCareNeedDes = careSeeker.getCareNeedsDescription().stream().map((s1)->
//                s1.name()).collect(Collectors.toSet());
//
//        Set<String> seekerHealthCon = careSeeker.getHealthConditions().stream().map((s2)->
//                s2.name()).collect(Collectors.toSet());
//
//        //bat dau loc het giver tu database, neu loc ra kqua true thi +20 point
//            giversDB.forEach((giverDB) ->{
//            int pointEarned = 0;
//
//            Set<String> giverSkills = giverDB.getSkills().stream().map((t1)->
//                    t1.getSkillName().name()).collect(Collectors.toSet());
//
//            Set<String> giverCerts = giverDB.getCertifications().stream().map((t2)->
//                    t2.getCertificateName()).collect(Collectors.toSet());
//
//            String giverGender = giverDB.getUser().getGender().name();
//
//            String giverAddress = giverDB.getUser().getAddress();
//
//            if(checkSuitable(seekerCareNeedDes, giverSkills, "skill")){
//                pointEarned += 20;
//            }
//            if(checkSuitable(seekerHealthCon, giverCerts, "health")){
//                pointEarned += 20;
//            }
//            if(checkSuitable(careSeeker.getPreferredGiverGender(), giverGender, "gender")){
//                pointEarned += 20;
//            }
//            if(checkSuitable(careSeeker.getUser().getAddress(), giverAddress, "address")){
//                pointEarned += 20;
//            }
//
//            logging.info("The points found: " + pointEarned);
//
//            if(pointEarned >= 60){
//                giversSuitable.add(giverDB);
//                aiRecommendationRepository.save(
//                        AIRecommendation.builder()
//                                .matchPoint(pointEarned)
//                                .careGiver(giverDB)
//                                .careSeeker(careSeeker)
//                                .build());
//            }
//        });
//            return giversSuitable;
//    }
//
//
//    private boolean checkSuitable(Object seekerData, Object giverData, String type){
//        String prompt = buildPrompt(seekerData, giverData, type);
//
//        String result = appConfig.getAiMatchingResult(prompt);
//
//        logging.info("Prompt received: " + prompt);
//        return result.toLowerCase().contains("yes") || result.toLowerCase().contains("phù hợp") || result.toLowerCase().contains("match"); //AI no loc ve 1 doan text, kiem tra xem doan text do co "dong y" giua cac truong cua seeker va giver khong
//    }
//
//    //tao prompt xong gui len AI loc
//    private String buildPrompt(Object seekerData, Object giverData, String type){
//        String promptType = null;
//
//        switch (type){
//            case "skill":
//                promptType = "CareSeeker cần: " + seekerData + ". CareGiver có kỹ năng: " + giverData +
//                        ". Những kỹ năng này có phù hợp với nhu cầu không? Trả lời yes hoặc no.";
//                break;
//            case "health":
//                promptType = "CareSeeker có tình trạng sức khỏe: " + seekerData +
//                        ". CareGiver có chứng chỉ/kinh nghiệm: " + giverData +
//                        ". Họ có phù hợp để chăm sóc không? Trả lời yes hoặc no.";
//                break;
//            case "gender":
//                promptType = "CareSeeker muốn người chăm sóc giới tính: " + seekerData +
//                        ". CareGiver là: " + giverData +
//                        ". Có phù hợp không? Trả lời yes hoặc no.";
//                break;
//            case  "address":
//                promptType = "CareSeeker sống tại: " + seekerData +
//                        ". CareGiver sống tại: " + giverData +
//                        ". Hai địa điểm này có cùng thành phố không? Trả lời yes hoặc no.";
//                break;
//            default:
//                promptType = "Hãy so sánh " + seekerData + " và " + giverData + ". Chúng có phù hợp không?";
//        }
//        return promptType;
//    }


    /*
    -Ý tưởng:
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
