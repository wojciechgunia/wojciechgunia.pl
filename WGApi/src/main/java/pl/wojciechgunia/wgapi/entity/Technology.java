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
@Table(name = "technology")
@Entity
public class Technology {
    @Id
    private String name;
    private String image_url;

    @ManyToMany(mappedBy = "technologies", fetch = FetchType.LAZY)
    private Set<TlPost> tlPosts;
}
