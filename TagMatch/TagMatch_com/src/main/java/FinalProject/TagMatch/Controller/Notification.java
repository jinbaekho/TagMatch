package FinalProject.TagMatch.Controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Notification {

    String message = "";
    String href = "";

    public Notification(String message, String href) {
        this.message = message;
        this.href = href;
    }
}
