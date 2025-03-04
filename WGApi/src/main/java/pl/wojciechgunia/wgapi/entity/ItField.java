package pl.wojciechgunia.wgapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "it_field")
@Entity
public class ItField {
    @Id
    private String name;
    private String image_url;
    private String lang;

    @ManyToMany(mappedBy = "itFields", fetch = FetchType.LAZY)
    private Set<TlPost> tlPosts;
}
