package FinalProject.TagMatch.Repository;

import FinalProject.TagMatch.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    User findByEmailAndName(String email, String name);
    boolean existsByEmail(String Email);
    void deleteById(Long userId);
}