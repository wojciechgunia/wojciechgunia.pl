package pl.wojciechgunia.wgapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Table(name = "file_data")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(generator = "image_data_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "image_data_id_seq",sequenceName = "image_data_id_seq", allocationSize = 1)
    private long id;
    private String uuid;
    private String lang;
    private String path;
    private String type;
    @Column(name = "is_used")
    private boolean isUsed;
    @Column(name = "create_at")
    private LocalDate createAt;
}
