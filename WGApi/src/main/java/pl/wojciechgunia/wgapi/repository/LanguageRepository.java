package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
    void deleteByCode(String code);
}
