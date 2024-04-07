package FinalProject.TagMatch.Controller;

import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.Service.BookMarkService;
import FinalProject.TagMatch.DTO.ContentsDTO;
import FinalProject.TagMatch.Service.ContentsService;
import FinalProject.TagMatch.Service.MypageService;
import FinalProject.TagMatch.Service.TagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final MypageService mypageService;
    private final BookMarkService bookMarkService;
    private final ContentsService contentsService;

    @GetMapping("/tagpage") //url
    public Object tagPage(@RequestParam(name="tagname",defaultValue = "")String tagname , Model model, HttpSession session, HttpServletRequest request, ModelAndView mav){

        boolean isBookMark = false;
        SessionUser sessionUser = (SessionUser) session.getAttribute("user");

                tagService.makeRTagList(tagname);
        if (!tagname.isEmpty()){
            try{
                model.addAttribute("Ctagname",tagname);
                model.addAttribute("Rtags", tagService.getRTag(tagname));
                List<ContentsDTO> likeRank = contentsService.getLikeRank(tagname);
                model.addAttribute("iList",contentsService.topThreeMaking(likeRank,"i"));
                model.addAttribute("yList",contentsService.topThreeMaking(likeRank,"y"));
                model.addAttribute("tList",contentsService.topThreeMaking(likeRank,"t"));
            } catch (Exception e) {
                String referer = request.getHeader("Referer");
                mav.addObject("data",new Notification("찾으신 데이터가 없습니다", referer));
                mav.setViewName("Notification");
                return mav;

            }

        }


        if(sessionUser != null){
            mypageService.addSearchList(sessionUser,tagname);
            isBookMark = bookMarkService.isAleadyBookMark(tagname, sessionUser);
        }

        model.addAttribute("isBookMark", isBookMark);

        return "tagpage"; //html이름
    }
}
