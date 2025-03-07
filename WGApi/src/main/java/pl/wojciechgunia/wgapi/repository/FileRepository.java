package pl.wojciechgunia.wgapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wojciechgunia.wgapi.entity.FileEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByUuid(String uid);

    @Query(nativeQuery = true,value = "SELECT * FROM file_data WHERE create_at < current_date - interval '2 days' and is_used = false")
    List<FileEntity> findDontUseImages();
}
