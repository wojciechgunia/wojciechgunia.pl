package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.ItField;

import java.util.Optional;

@Repository
public interface ItFieldRepository extends JpaRepository<ItField, String> {
    void deleteByName(String name);
    Optional<ItField> findItFieldByName(String name);
}
