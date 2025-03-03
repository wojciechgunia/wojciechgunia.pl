package pl.wojciechgunia.wgapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentFileDTO {
    private String code;
    private String type;
    private String file_url;
}
