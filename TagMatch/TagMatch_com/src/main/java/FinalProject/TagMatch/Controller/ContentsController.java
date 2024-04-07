package FinalProject.TagMatch.Controller;

import FinalProject.TagMatch.Service.ContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class ContentsController {

    @Autowired
    private final ContentsService contentsService;

    /**
     * rankpage main & table
     *
     * @param radioValue    the radio value is days (7 or 30)
     * @param checkboxValue the checkbox value is platformid (i or t or y)
     * @param model         the model
     * @return 'rankpage'
     */
    @GetMapping("/rankpage")
    public String tagRankPage(@RequestParam(name = "days" ,required = false, defaultValue = "7") int radioValue,
                              @RequestParam(name = "platform", required = false, defaultValue = "i") List<String> checkboxValue,
                              Model model) {

        String[] getGtags = contentsService.getPlatformRank(radioValue, checkboxValue);
        Map<String,Integer> map = contentsService.gTagCount(getGtags);
        Map<String,Integer> sortMap = contentsService.sortByValueDesc(map);
        model.addAttribute("platformtags",sortMap.entrySet());

        return "rankpage";
    }

    /**
     * category main & table
     *
     * @param radioValue the radio value is categorys (1 ~ 14)
     * @param model      the model
     * @return 'categorypage'
     */
    @GetMapping("/categorypage")
    public String categoryPage(@RequestParam(name = "category", required = false,defaultValue = "1") int radioValue, Model model){


        String[] getGtags = contentsService.categoryTags(radioValue);
        Map<String,Integer> map = contentsService.gTagCount(getGtags);
        Map<String,Integer> sortMap = contentsService.sortByValueDesc(map);
        model.addAttribute("categorytags",sortMap.entrySet());

        return "categorypage";




    }

}

