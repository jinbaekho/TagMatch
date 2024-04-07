package FinalProject.TagMatch.Controller;

import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.DTO.UserBookMarkDto;
import FinalProject.TagMatch.Service.BookMarkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional
public class BookMarkController {

    private final BookMarkService bookMarkService;

    /**
     * add bookmark
     * @param tagname tagname to add bookmark
     * @param session
     * @param request
     * @param model
     * @param mav
     * @return
     */
    @GetMapping("/bookmark")
    public ModelAndView bookMark(@RequestParam(name="tagname")String tagname, HttpSession session, HttpServletRequest request, Model model, ModelAndView mav){

        SessionUser user = (SessionUser) session.getAttribute("user");
        String userEmail = user.getEmail();
        String userName = user.getName();

        bookMarkService.addBookMark(userEmail, userName, tagname, model);

        String referer = request.getHeader("Referer");

        mav.addObject("data", new Notification("즐겨찾기를 추가했습니다!", referer));
        mav.setViewName("Notification");

        return mav;
    }

    /**
     * get user bookmark list
     * @param session
     * @return send UserBookMarkDto list to mypage
     */
    public List<UserBookMarkDto> getUserBookMark(HttpSession session){

        SessionUser user = (SessionUser) session.getAttribute("user");
        String userEmail = user.getEmail();
        String userName = user.getName();

        return bookMarkService.getUserBookMarks(userEmail, userName);
    }

    /**
     * remove bookmark
     * @param tagname tagname to remove bookmark
     * @param session
     * @param request
     * @param mav
     * @return
     */
    @GetMapping("/remove/bookmark")
    public ModelAndView removeUserBookMark(@RequestParam(name="tagname")String tagname, HttpSession session, HttpServletRequest request, ModelAndView mav){

        SessionUser user = (SessionUser) session.getAttribute("user");
        String userEmail = user.getEmail();
        String userName = user.getName();

        bookMarkService.removeUserBookMark(tagname, userEmail, userName);

        String referer = request.getHeader("Referer");

        mav.addObject("data", new Notification("즐겨찾기를 제거했습니다!", referer));
        mav.setViewName("Notification");

        return mav;
    }

}
