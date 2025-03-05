package pl.wojciechgunia.wgapi.entity;

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
public class TlPostDTO {
    private String uuid;
    private String lang;
    private String name;
    private String type;
    private String descShort;
    private String descHtml;
    private List<String> imageUrls;
    private List<String> fileUrls;
    private boolean header_img;
    private char slider;
    private LocalDate createAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private AuthorDTO createBy;
    private Set<Technology> technologies;
    private Set<ItField> itFields;
}
