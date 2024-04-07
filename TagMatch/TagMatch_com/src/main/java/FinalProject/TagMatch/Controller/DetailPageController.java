package FinalProject.TagMatch.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DetailPageController {

    @GetMapping("/detailpage")
    public static String detailPage(@RequestParam(name="tagname", defaultValue = "") String tagname, Model model){

        if (!tagname.isEmpty()){
            model.addAttribute("Ctagname",tagname);
        }
        return "detailpage";
    }
}
