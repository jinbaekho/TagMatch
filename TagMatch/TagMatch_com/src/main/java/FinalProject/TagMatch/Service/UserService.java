package FinalProject.TagMatch.Service;

import FinalProject.TagMatch.DTO.SessionUser;
import FinalProject.TagMatch.Entity.User;
import FinalProject.TagMatch.Repository.BookMarkRepository;
import FinalProject.TagMatch.Repository.HistoryRepository;
import FinalProject.TagMatch.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final BookMarkRepository bookMarkRepository;

    @Autowired
    private PasswordEncoder pwEncoder;


    @Transactional
    public void signUp(User user){
        userRepository.save(user);
    }

    /**
     * Handles validation errors by creating a map of error messages.
     *
     * @param errors The errors to be handled
     * @return Map containing validation error messages
     */
    // 회원가입시 유효성 체크 및 중복 조회 처리
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        for(FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    /**
     * Performs user login validation and manages the session.
     *
     * @param email   The email for login
     * @param passwd  The password for login
     * @param session The HttpSession object
     * @return True if login is successful; False otherwise
     */
    // 로그인 확인 서비스
    public boolean loginService(String email, String passwd, HttpSession session){
        Optional<User> user = userRepository.findByEmail(email);
        String rawPwd = "";
        String encodePw = "";

        if(user.isPresent()){
            rawPwd = passwd;
            encodePw = user.get().getPasswd();

            if (pwEncoder.matches(rawPwd, encodePw)) {
                session.setAttribute("user", new SessionUser(user.get()));
                return true;
            }
        }
        return false;
    }


    // 회원탈퇴
    public void Withdrawal(Long userId, User user){
        try {
            userRepository.deleteById(userId);
            historyRepository.deleteByUserId(userId);
            bookMarkRepository.deleteByUser(user);

        } catch (Exception ignored) {

        }
    }

    public User userIdToEntity(String userEmail, String userName){
        User user = userRepository.findByEmailAndName(userEmail, userName);
        return user;
    }
}
