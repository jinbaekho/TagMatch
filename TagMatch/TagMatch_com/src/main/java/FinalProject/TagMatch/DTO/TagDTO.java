package FinalProject.TagMatch.DTO;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagDTO {
    private String tagid;
    private String rtags;
    private int tagcount;

    public TagDTO (String tagid, int tagcount){
        this.tagid= tagid;
        this.tagcount= tagcount;
    }
}
