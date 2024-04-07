package FinalProject.TagMatch.Repository;

import FinalProject.TagMatch.DTO.UserBookMarkDto;
import FinalProject.TagMatch.Entity.User;
import FinalProject.TagMatch.Entity.UserBookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<UserBookMark, Integer> {

    List<UserBookMark> findByUser(User user);

    void deleteByBookMarkAndUser(String BookMark, User user);

    void deleteByUser(User user);

    boolean existsByBookMarkAndUser(String BookMark, User user);

    Long countByUser(User user);
}
