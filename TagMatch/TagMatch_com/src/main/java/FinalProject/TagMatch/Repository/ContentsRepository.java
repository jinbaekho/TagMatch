package FinalProject.TagMatch.Repository;

import FinalProject.TagMatch.Entity.Contents;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContentsRepository extends JpaRepository<Contents, Integer > {
    List<Contents> findAllByOrderByPlatformid();
    List<Contents> findByCrawlingdateBetween(LocalDate startDate, LocalDate endDate);
    List<Contents> findAllByGtagsContains(String tagName, Sort like);
    List<Contents> findAllByGtagsContains(String tagName);
    List<Contents> findByCategoryid(int categoryid);
}
