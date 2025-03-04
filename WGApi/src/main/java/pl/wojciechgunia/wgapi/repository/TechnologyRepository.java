package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.Technology;

import java.util.Optional;

@Repository
public interface TechnologyRepository  extends JpaRepository<Technology, String> {
    void deleteByName(String name);
    Optional<Technology> findTechnologiesByName(String name);
}
