package FinalProject.TagMatch.DTO;

import FinalProject.TagMatch.Entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserBookMarkDto {

    private String bookMark;
    private int priority;


    public UserBookMarkDto(String bookMark) {
        this.bookMark = bookMark;
    }
}
