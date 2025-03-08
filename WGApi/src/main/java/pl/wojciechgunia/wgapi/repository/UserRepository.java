package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String username);

    Optional<User> findUserByEmail(String username);
    Optional<User> findUserByUuid(String uuid);
    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and is_lock=false and is_enabled=true")
    Optional<User> findUserByLoginAndLockAndEnabled(String login);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and is_lock=false and is_enabled=true and role='ADMIN'")
    Optional<User> findUserByLoginAndLockAndEnabledAndIsAdmin(String subject);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and is_lock=false and is_enabled=true and role='USER'")
    Optional<User> findUserByLoginAndLockAndEnabledAndIsUser(String subject);

    Optional<User> findUserByLoginAndUuid(String login, String uuid);
}