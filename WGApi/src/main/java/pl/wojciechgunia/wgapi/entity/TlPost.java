package pl.wojciechgunia.wgapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "timeline_posts")
@Entity
public class TlPost {
    @Id
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="users_id_seq", sequenceName = "users_id_seq",allocationSize = 1)
    private long id;
    private String uuid;
    private String lang;
    private String name;
    private String type;
    private String descShort;
    private String descHtml;
    @ElementCollection
    private List<String> imageUrls;
    @ElementCollection
    private List<String> fileUrls;
    private boolean header_img;
    private char slider;
    private LocalDate createAt;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "create_by")
    private User createBy;
    @ManyToMany
    @JoinTable(
            name = "technology_to_post",
            joinColumns = @JoinColumn(name = "id_post"),
            inverseJoinColumns = @JoinColumn(name = "name_technology"))
    private Set<Technology> technologies;
    @ManyToMany
    @JoinTable(
            name = "field_to_post",
            joinColumns = @JoinColumn(name = "id_post"),
            inverseJoinColumns = @JoinColumn(name = "name_it_field"))
    private Set<ItField> itFields;
}
