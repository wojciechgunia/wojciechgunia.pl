package pl.wojciechgunia.wgapi.translator;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import pl.wojciechgunia.wgapi.entity.Content;
import pl.wojciechgunia.wgapi.entity.ContentTextDTO;

@Mapper(componentModel = "spring")
public abstract class ContentToContentTextDTO {
    public ContentTextDTO toContentTextDTO(Content content) {
        return toDTO(content);
    }

    @Mappings({})
    protected abstract ContentTextDTO toDTO(Content content);
}
