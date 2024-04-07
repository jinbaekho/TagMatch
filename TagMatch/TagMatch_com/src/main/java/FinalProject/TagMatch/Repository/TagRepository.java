package FinalProject.TagMatch.Repository;

import FinalProject.TagMatch.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    List<Tag> findAllByOrderByTagcountDesc();
    @Transactional
    @Modifying
    @Query("UPDATE Tag t SET t.rtags = :newRtags WHERE t.tagid = :tagId")
    void updateRtagsByTagId(String tagId, String newRtags);

}
