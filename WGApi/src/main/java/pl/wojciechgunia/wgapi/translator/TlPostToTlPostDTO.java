package pl.wojciechgunia.wgapi.translator;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import pl.wojciechgunia.wgapi.entity.*;

@Mapper(componentModel = "spring")
public abstract class TlPostToTlPostDTO {
    public TlPostDTO toTlPostDTO(TlPost tlPost) {
        return toDTO(tlPost);
    }

    @Mappings({
            @Mapping(source = "createBy", target = "createBy", qualifiedByName = "mapCreateBy")
    })
    protected abstract TlPostDTO toDTO(TlPost tlPost);

    @Named("mapCreateBy")
    AuthorDTO mapCreateBy(User user) {
        return user != null ? new AuthorDTO(user.getLogin(), user.getName(), user.getSurname()) : null;
    }
}