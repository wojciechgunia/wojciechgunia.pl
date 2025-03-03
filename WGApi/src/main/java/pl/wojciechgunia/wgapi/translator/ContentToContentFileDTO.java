package pl.wojciechgunia.wgapi.translator;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import pl.wojciechgunia.wgapi.entity.Content;
import pl.wojciechgunia.wgapi.entity.ContentFileDTO;

@Mapper(componentModel = "spring")
public abstract class ContentToContentFileDTO {
    public ContentFileDTO toContentFileDTO(Content content) {
        return toDTO(content);
    }

    @Mappings({})
    protected abstract ContentFileDTO toDTO(Content content);
}
