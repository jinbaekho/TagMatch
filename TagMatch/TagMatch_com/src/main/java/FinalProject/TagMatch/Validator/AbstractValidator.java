package FinalProject.TagMatch.Validator;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Slf4j
public abstract class AbstractValidator<T> implements Validator {

    /**
     * Indicates unconditional support for validating any class type.
     *
     * @param clazz The class to check for support
     * @return boolean indicating universal support for validation
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * Validates the target object and catches runtime exceptions.
     *
     * @param target The object to validate
     * @param errors Object to register errors (if any)
     */
    @Override
    public void validate(Object target, Errors errors) {
        try{
            doValidate((T) target, errors);
        } catch (RuntimeException e) {
            log.error("중복 검증 에러", e);
            throw e;
        }
    }

    /**
     * Performs specific validation logic for the provided DTO object.
     * Checks for duplicates or specific constraints and registers errors if needed.
     *
     * @param dto     The DTO object to validate
     * @param errors  Object to register errors (if any)
     */
    protected abstract void doValidate(final T dto, final Errors errors);
}