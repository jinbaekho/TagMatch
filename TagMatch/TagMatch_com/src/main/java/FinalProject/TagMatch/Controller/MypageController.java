package FinalProject.TagMatch.Controller;

import FinalProject.TagMatch.DTO.HistoryDto;
import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.DTO.UserBookMarkDto;
import FinalProject.TagMatch.Service.MypageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;
    private final BookMarkController bookMarkController;

    @GetMapping("/mypage")
    public String myPage(Model model, HttpServletRequest req, HttpSession session) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("user");

        String userEmail = sessionUser.getEmail();

        long userId = mypageService.getId(userEmail);

        List<HistoryDto> searchhistoryList = mypageService.getSearchList(userId);
        model.addAttribute("searchhistoryList", searchhistoryList);

        List<UserBookMarkDto> userBookMarkDtoList = bookMarkController.getUserBookMark(session);

        model.addAttribute("userBookMarkDtoList", userBookMarkDtoList);

        return "mypage"; // HTML 파일 이름
    }

    @GetMapping("/remove/history")
    public String removeHistory(@RequestParam(name="tagname")String tagname, HttpSession session, HttpServletRequest request){

        SessionUser user = (SessionUser) session.getAttribute("user");
        String userEmail = user.getEmail();

        mypageService.removeHistory(tagname, userEmail);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

}