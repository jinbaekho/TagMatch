package FinalProject.TagMatch.Controller;

import FinalProject.TagMatch.DTO.MemberFormDto;
import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.Entity.User;
import FinalProject.TagMatch.Reference.Role;
import FinalProject.TagMatch.Service.UserService;
import FinalProject.TagMatch.Validator.CheckEmailValidator;
import FinalProject.TagMatch.Validator.CheckPasswordEqualValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Login controller.
 */
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final CheckEmailValidator checkEmailValidator;
    private final CheckPasswordEqualValidator checkPasswordEqualValidator;
    @Autowired
    private PasswordEncoder pwEncoder;

    /**
     * For validation
     *
     * @param
     *
     */
    // 커스텀 유효성 검증을 위해 추가
    @InitBinder
    public void validatorBinder(WebDataBinder binder){
        binder.addValidators(checkEmailValidator);
        binder.addValidators(checkPasswordEqualValidator);
    }

    /**
     * handles user login functionality.
     * It retrieves the user's email and password to attempt a login.
     * Upon successful login, it retrieves the previous page URL.
     *
     * @param request The current HTTP request object
     * @param session The current session object
     * @return ResponseEntity containing login result
     */
    @PostMapping("/login/user")
    public ResponseEntity<Map<String, Object>> goLoginpage(HttpServletRequest request, HttpSession session){
        String email = request.getParameter("email");
        String pwd = request.getParameter("passwd");

        boolean loginResult = userService.loginService(email, pwd, session);

        Map<String, Object> response = new HashMap<>();
        if (loginResult) {

            // 로그인 성공시 이전 페이지 URL을 가져옴
            String referer = request.getHeader("Referer");
            response.put("loginSuccess", loginResult);
            return ResponseEntity.ok(response);

        }

        // 로그인 실패시
        response.put("loginFail", loginResult);
        return ResponseEntity.ok(response);

    }

    /**
     * Go signup string.
     *
     * @return the string
     */
    @GetMapping("/signup")
    public String goSignup(Model model) {
        model.addAttribute("MemberFormDto", new MemberFormDto());
        return "singup";
    }

    /**
     * Go signup string.
     *
     * @return the string
     */
    // 프론트에서 모델을 받아 th:if를 사용하는 코드가 먹지 않는 관계로 스크립트 fetch()를 사용하여 처리하도록 하겠음.(그렇게 처리하면
    // 회원가입 실패시 입력 데이터 값을 유지가 어려움.)
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> createUser(@Valid MemberFormDto memberForm, Errors errors, Model model, HttpServletRequest request)
            throws Exception {

        Map<String, Object> response = new HashMap<>();
        if(errors.hasErrors()){
            // 회원가입 실패시 입력 데이터 값을 유지
            model.addAttribute("MemberFormDto", memberForm);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for(String key : validatorResult.keySet()){
                response.put(key, validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴
            return ResponseEntity.ok(response);
        }

        User user = User.builder().name(memberForm.getName())
                .email(memberForm.getEmail())
                .passwd(pwEncoder.encode(memberForm.getPasswd()))
                .role(Role.LOCAL_USER)
                .build();
        userService.signUp(user);

        response.put("success","success");

        return ResponseEntity.ok(response);
    }

    /**
     * Handles the withdrawal process for a user.
     * Clears the search history and deletes bookmarks upon user withdrawal.
     *
     * @param session The HttpSession object
     * @param model   The Model object to add attributes
     * @return The view name "mainpage"
     */
    @GetMapping("/Withdrawal")
    public String Withdrawal(HttpSession session, Model model){
        SessionUser user = (SessionUser) session.getAttribute("user");
        String userEmail = user.getEmail();
        String userName = user.getName();

        User dUser = userService.userIdToEntity(userEmail,userName);
        userService.Withdrawal(dUser.getUserId(), dUser);

        session.removeAttribute("user");
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");

        return "redirect:/mainpage";
    }

}
