package pl.wojciechgunia.wgapi.translator;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import pl.wojciechgunia.wgapi.entity.Content;
import pl.wojciechgunia.wgapi.entity.ContentDTO;

@Mapper(componentModel = "spring")
public abstract class ContentDTOToContent {
    public Content toContent(ContentDTO contentDTO) {
        return toCont(contentDTO);
    }

    @Mappings({})
    protected abstract Content toCont(ContentDTO contentDTO);
}
