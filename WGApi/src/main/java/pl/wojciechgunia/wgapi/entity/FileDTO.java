package pl.wojciechgunia.wgapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FileDTO {
    private String uid;
    private LocalDate createAt;
    private String lang;
    private String type;
}
