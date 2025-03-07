package pl.wojciechgunia.wgapi.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.wojciechgunia.wgapi.entity.FileEntity;
import pl.wojciechgunia.wgapi.repository.FileRepository;


import java.io.IOException;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FTPService ftpService;

    @PersistenceContext
    EntityManager entityManager;

    public void save(FileEntity fileEntity) {
        fileRepository.saveAndFlush(fileEntity);
    }

    public FileEntity findByUid(String uid) {
        return fileRepository.findByUuid(uid).orElse(null);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public  void cleanImages() {
        fileRepository.findDontUseImages().forEach(value ->{
            try {
                ftpService.deleteFile(value.getPath());
                fileRepository.delete(value);
            } catch (IOException e) {
                System.out.println("Cant delete "+value.getUuid());
            }
        });
    }

    public List<FileEntity> getFiles(int page, int limit) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileEntity> query = criteriaBuilder.createQuery(FileEntity.class);
        Root<FileEntity> root = query.from(FileEntity.class);
        page=lowerThanOne(page);
        limit=lowerThanOne(limit);
        String column = "createAt";
        Order orderQuery = criteriaBuilder.desc(root.get(column));
        query.orderBy(orderQuery);
        return entityManager.createQuery(query).setFirstResult((page-1)*limit).setMaxResults(limit).getResultList();
    }

    private int lowerThanOne(int number) {
        return Math.max(number, 1);
    }

    public long countFiles() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<FileEntity> root = query.from(FileEntity.class);
        query.select(criteriaBuilder.count(root));
        return entityManager.createQuery(query).getSingleResult();
    }
}
