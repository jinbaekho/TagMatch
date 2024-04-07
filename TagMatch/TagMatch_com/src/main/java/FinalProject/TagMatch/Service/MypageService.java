package FinalProject.TagMatch.Service;

import FinalProject.TagMatch.DTO.HistoryDto;
import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.Entity.User;
import FinalProject.TagMatch.Entity.UserDetail;
import FinalProject.TagMatch.Repository.BookMarkRepository;
import FinalProject.TagMatch.Repository.HistoryRepository;
import FinalProject.TagMatch.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import  jakarta.persistence.EntityNotFoundException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    public Long getId(String email){
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        Long userID = user.getUserId();

        return userID;
    }

    public List<HistoryDto> getSearchList(Long userID){
        List<UserDetail> userSearchList = historyRepository.findByUserId(userID);
        List<HistoryDto> searchListDto = new ArrayList<>();

        for(UserDetail us: userSearchList){
            HistoryDto sDto = new HistoryDto(
                    us.getLastSearch(),
                    us.getSearchDate()
            );
            searchListDto.add(sDto);
        }
        return searchListDto;
    }

    /**
     * add search history
     * @param sessionUser
     * @param tagname
     */
    public void addSearchList(SessionUser sessionUser, String tagname) {
//        유저 이메일을 받아 UserId로 변환후 검색기록 갯수확인
        Optional<User> user = userRepository.findByEmail(sessionUser.getEmail());
        long userId = user.get().getUserId();
        List<UserDetail> historyList = historyRepository.findByUserId(userId, Sort.by(Sort.Direction.ASC, "searchDate"));


//      tagname 중복 확인후 갱신
        for (UserDetail userDetail : historyList) {
            if (userDetail.getLastSearch().equals(tagname)) {
                System.out.printf("중복확인");
                historyRepository.delete(userDetail);
                historyList = historyRepository.findByUserId(userId, Sort.by(Sort.Direction.ASC, "searchDate"));
                break;
            }
        }

//      저장갯수를 넘어서면 가장 오래된 데이터를 삭제
        long count = historyList.size();
        if (count > 9) {
            System.out.printf("오버확인");
            historyRepository.delete(historyList.get(0));
        }
        UserDetail userDetail = new UserDetail();

        // 오늘 날짜
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(date);

        userDetail.setUserId(userId);
        userDetail.setLastSearch(tagname);
        userDetail.setSearchDate(strDate);

        historyRepository.save(userDetail);
        System.out.println("저장 성공");


        historyList = historyRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "searchDate"));
        System.out.printf("출력"+historyList.toString());
    }

    /**
     * remove search history
     * @param tagname tagname to remove
     * @param userEmail user to remove search history
     */
    @Transactional
    public void removeHistory(String tagname, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        long userId = user.get().getUserId();

        historyRepository.deleteByLastSearchAndUserId(tagname, userId);
    }
}
