package pl.wojciechgunia.wgapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "content")
@Entity
public class Content {
    @Id
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="users_id_seq", sequenceName = "users_id_seq",allocationSize = 1)
    private long id;
    private String code;
    private String site;
    private String lang;
    private String type;
    private String text;
    @Column(name = "file_url")
    private String file_url;
}
