package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.TlPost;

import java.util.Optional;

@Repository
public interface TlPostRepository extends JpaRepository<TlPost, Long> {
    Optional<TlPost> findByUuid(String uuid);
    void deleteByUuid(String uuid);
}
