package pl.wojciechgunia.wgapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentTextDTO {
    private String code;
    private String lang;
    private String type;
    private String text;
}
