package FinalProject.TagMatch.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag")
@ToString
public class Tag {

    @Id
    private String tagid;
    private String rtags;
    private int tagcount;
}
