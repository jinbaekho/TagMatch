package FinalProject.TagMatch.Validator;

import FinalProject.TagMatch.DTO.MemberFormDto;
import FinalProject.TagMatch.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckEmailValidator extends AbstractValidator<MemberFormDto>{
    private final UserRepository userRepository;
    @Override
    protected void doValidate(MemberFormDto dto, Errors errors) {
        if(userRepository.existsByEmail(dto.getEmail())){
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다.");
        }
    }
}
