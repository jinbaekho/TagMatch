package FinalProject.TagMatch.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER-BOOKMARK")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserBookMark {
    @Id
    @Column(name="BOOKMARKNUMBER")
    private int BookMarkNum;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
    private String bookMark;
    private String markDate;
    private int priority;
}
