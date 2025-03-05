package pl.wojciechgunia.wgapi.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.wojciechgunia.wgapi.entity.*;
import pl.wojciechgunia.wgapi.repository.TlPostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimelineService {
    public final TlPostRepository tlPostRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<TlPost> getTimelineList(String nameLike, String type, String technologies, String itFields, int page, int limit, String order) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TlPost> query = criteriaBuilder.createQuery(TlPost.class);
        Root<TlPost> root = query.from(TlPost.class);

        page=lowerThanOne(page);
        limit=lowerThanOne(limit);

        List<Predicate> predicates = prepareQuery(criteriaBuilder, root, nameLike, type, technologies, itFields);

        if(!order.isEmpty()) {
            Order orderQuery;
            if(order.equals("desc")) {
                orderQuery = criteriaBuilder.desc(root.get("endDate"));
            } else {
                orderQuery = criteriaBuilder.asc(root.get("endDate"));
            }
            query.orderBy(orderQuery);
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).setFirstResult((page-1)*limit).setMaxResults(limit).getResultList();
    }

    private int lowerThanOne(int number)
    {
        return Math.max(number, 1);
    }

    private List<Predicate> prepareQuery(CriteriaBuilder criteriaBuilder,Root<TlPost> root, String name, String type, String technologies, String itFields) {
        List<Predicate> predicates = new ArrayList<>();
        if(name != null && !name.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if(type != null && !type.isEmpty()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("type")), type.toLowerCase()));
        }
        if(technologies != null && !technologies.isEmpty()) {
            Join<TlPost, Technology> techJoin = root.join("technologies");
            predicates.add(techJoin.get("name").in((Object) technologies.split(";")));
        }
        if(itFields != null && !itFields.isEmpty()) {
            Join<TlPost, ItField> fieldJoin = root.join("itFields");
            predicates.add(fieldJoin.get("name").in((Object) itFields.split(";")));
        }
        return predicates;
    }

    public TlPost getTimelinePost(String uuid) {
        return this.tlPostRepository.findByUuid(uuid).orElseThrow();
    }

    public ResponseEntity<?> postTimelinePost(TlPost tlPost) {
        try {
            if(tlPost.getUuid().isEmpty()) {
                tlPost.setUuid(UUID.randomUUID().toString());
            }
            this.tlPostRepository.save(tlPost);return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }

    }

    public ResponseEntity<?> putTimelinePost(TlPost tlPost) {
        if(this.tlPostRepository.findByUuid(tlPost.getUuid()).isPresent()) {
            try {
                this.tlPostRepository.save(tlPost);
                return ResponseEntity.ok(new Response(Code.SUCCESS));
            } catch (Exception e) {
                return ResponseEntity.status(400).body(new Response(Code.E2));
            }
        } else {
            return ResponseEntity.status(400).body(new Response(Code.E4));
        }
    }

    public ResponseEntity<?> deleteTimelinePost(String uuid) {
        try {
            this.tlPostRepository.deleteByUuid(uuid);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }
}
