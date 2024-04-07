package FinalProject.TagMatch.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContentsDTO {

    private int contentsnumber;
    private String platformid;
    private String writer;
    private LocalDate date;
    private int likes;
    private String gtags;
    private int commments;
    private String url;
    private String content;
    private LocalDate crawlingdate;
    private int categoryid;

    /**
     *
     * @param url
     * @param likes
     * @param content
     * @param platformid
     */
    public ContentsDTO(String url, int likes, String content, String platformid) {
        this.platformid = platformid;
        this.content = content;
        this.url = url;
        this.likes = likes;
    }

    public ContentsDTO(String gtags, int categoryid) {
        this.gtags = gtags;
        this.categoryid = categoryid;
    }
}
