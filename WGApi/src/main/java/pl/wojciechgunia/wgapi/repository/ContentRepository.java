package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.Content;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findContentByCodeAndLang(String code, String lang);

    Optional<List<Content>> findContentBySiteAndLang(String site, String lang);
}
