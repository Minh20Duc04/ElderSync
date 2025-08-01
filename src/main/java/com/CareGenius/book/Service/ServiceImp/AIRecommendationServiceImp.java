package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Repository.AIRecommendationRepository;
import com.CareGenius.book.Repository.CareGiverRepository;
import com.CareGenius.book.Repository.CareSeekerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AIRecommendationServiceImp {

    private final AIRecommendationRepository aiRecommendationRepository;
    private final CareGiverRepository careGiverRepository;
    private final CareSeekerRepository careSeekerRepository;




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
