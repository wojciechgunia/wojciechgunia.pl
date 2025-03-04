package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.Types;

@Repository
public interface TypesRepository  extends JpaRepository<Types, String> {
}
