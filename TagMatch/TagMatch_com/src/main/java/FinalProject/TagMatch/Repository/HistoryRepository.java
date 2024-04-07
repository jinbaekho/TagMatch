package FinalProject.TagMatch.Repository;

import FinalProject.TagMatch.Entity.UserDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<UserDetail, String> {
//    List<UserDetail> findByUserNumber(Long userNumber);
    List<UserDetail> findByUserId(Long userId);
    List<UserDetail> findByUserId(Long userId, Sort historyNumber);
    void deleteByLastSearchAndUserId(String lastSearch, long userId);
    void deleteByUserId(Long userId);

}
