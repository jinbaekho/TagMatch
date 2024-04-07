package FinalProject.TagMatch.Service;

import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.DTO.UserBookMarkDto;
import FinalProject.TagMatch.Entity.User;
import FinalProject.TagMatch.Entity.UserBookMark;
import FinalProject.TagMatch.Repository.BookMarkRepository;
import FinalProject.TagMatch.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;

    /**
     * Add tag bookmark to database
     * @param userEmail
     * @param userName
     * @param tagname
     */
    public void addBookMark(String userEmail, String userName, String tagname, Model model){

//        Optional<User> user = userRepository.findByEmail(userEmail);
        User user = userRepository.findByEmailAndName(userEmail, userName);

        Long count = bookMarkRepository.countByUser(user);

        if(count < 5){
            UserBookMark userBookMark = new UserBookMark();

            // 오늘 날짜
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = format.format(date);

            userBookMark.setUser(user);
            userBookMark.setBookMark(tagname);
            userBookMark.setMarkDate(strDate);
            userBookMark.setPriority(1);

            bookMarkRepository.save(userBookMark);
        }
        else{
//            notificationController.showNotification(model, "즐겨찾기는 최대 5개까지 가능합니다.") ;
            System.out.println("저장 실패");
        }
    }

    /**
     * get user tag bookmark in database
     * @param userEmail
     * @param userName
     * @return: UserBookMarkDTO
     */
    public List<UserBookMarkDto> getUserBookMarks(String userEmail, String userName){

        User user = userRepository.findByEmailAndName(userEmail, userName);

        List<UserBookMark> userBookMarkList = bookMarkRepository.findByUser(user);

        List<UserBookMarkDto> userBookMarkDtoList = new ArrayList<>();

        for (UserBookMark userBookMark : userBookMarkList){

            UserBookMarkDto userBookMarkDto = new UserBookMarkDto(
                    userBookMark.getBookMark(),
                    userBookMark.getPriority()
            );
            userBookMarkDtoList.add(userBookMarkDto);
        }
        return userBookMarkDtoList;
    }

    /**
     * remove bookmark
     * @param tagname tagname to remove bookmark
     * @param userEmail user to remove bookmark
     * @param userName user to remove bookmark
     */
    @Transactional
    public void removeUserBookMark(String tagname, String userEmail, String userName){

        User user = userRepository.findByEmailAndName(userEmail, userName);

        bookMarkRepository.deleteByBookMarkAndUser(tagname ,user);
    }

    /**
     * check tagname if already add bookmark
     * @param tagname taname to check
     * @param sessionUser
     * @return
     */
    public boolean isAleadyBookMark(String tagname, SessionUser sessionUser){

        String userEmail = sessionUser.getEmail();
        String userName = sessionUser.getName();

        User user = userRepository.findByEmailAndName(userEmail, userName);

        return bookMarkRepository.existsByBookMarkAndUser(tagname, user);
    }
}
