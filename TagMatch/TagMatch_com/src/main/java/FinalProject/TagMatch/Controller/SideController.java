package FinalProject.TagMatch.Controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SideController {
    @GetMapping("/sidebar")
    public String sidebar(Model model,HttpSession session){
        return "sidebar";
    }

    @GetMapping("/developer")
    public String developer(Model model,HttpSession session){
        return "developer";
    }
}
