package FinalProject.TagMatch.Controller;

import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.Service.TagService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class MainController {


    @Autowired
    public final TagService TagService;

    /**
     * Going Enterpage
     *
     * @param model the model
     * @return 'enterpage'
     */
// 첫 입장 페이지
    @GetMapping("/enterpage")//url
    public String enterPage(Model model) {
        return "enterpage";//html이름
    }// 첫 입장 페이지

    /**
     * Reload redirect view.
     *
     * @param model the model
     * @return the redirect view
     */
    @GetMapping("/mainpage/reload")//url
    public RedirectView reload(Model model) {
        TagService.saveTagID(TagService.makeGTagList());
        return new RedirectView("/mainpage");//html이름
    }

    /**
     * mainpage main & table
     *
     * @param model   the model
     * @param tagid   the tagid is all tag & Desc tagcount
     * @param session the session
     * @return 'mainpage'
     */
//메인페이지 이동 함수
    @GetMapping("/mainpage")
    public String mainPage(Model model, String tagid, HttpSession session) {
        model.addAttribute("tagrank", TagService.taginfo(tagid));

        // 세션에서 사용자 정보 가져오기
        SessionUser user = (SessionUser) session.getAttribute("user");

        if (user != null) {
            session.setAttribute("userName", user.getName());
            session.setAttribute("role",user.getRole());
            model.addAttribute("userName", user.getName());
        }

        return "mainpage";
    }

    /**
     * Search string.
     *
     * @param keyword the keyword is SearchBar Text
     * @param model   the model
     * @return 'tagpage'
     */
    @GetMapping("/search") //@RequestParam = form action될때 넘어가는 이름
    public String search(@RequestParam("tagname") String keyword, Model model){
        return "tagpage";
    }
}

