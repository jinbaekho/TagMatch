package FinalProject.TagMatch.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

/**
 * The type Contents.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contents")
@ToString
public class Contents {

    /**
     * The Contentsnumber.
     */
    @Id
    int contentsnumber;
    /**
     * The Platformid t, y, i
     */
    String platformid;
    /**
     * The Writer. 작성자
     */
    String writer;
    /**
     * The Date. 작성날짜
     */
    LocalDate date;
    /**
     * The Likes. 좋아요 수
     */
    int likes;
    /**
     * The Gtags. 컨텐츠 내 태그 전체
     */
    String gtags;
    /**
     * The Comments. 댓글수
     */
    String comments;
    /**
     * The Url. 주소
     */
    String url;
    /**
     * The Content. 게시물 글
     */
    String content;
    /**
     * The Crawlingdate. 크롤링한 날짜
     */
    LocalDate crawlingdate;
    /**
     * The Categoryid. 분류된 카테고리 id 값
     */
    int categoryid;

}
