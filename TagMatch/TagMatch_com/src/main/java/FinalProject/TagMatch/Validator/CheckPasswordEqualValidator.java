package FinalProject.TagMatch.Validator;

import FinalProject.TagMatch.DTO.MemberFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CheckPasswordEqualValidator extends AbstractValidator<MemberFormDto>{
    @Override
    protected void doValidate(MemberFormDto dto, Errors errors) {
        if(!dto.getPasswd().equals(dto.getRePasswd())){
            errors.rejectValue("rePasswd", "비밀번호 일치 오류", "비밀번호가 일치하지 않습니다.");
        }
    }
}