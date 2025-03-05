package pl.wojciechgunia.wgapi.translator;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import pl.wojciechgunia.wgapi.entity.*;
import pl.wojciechgunia.wgapi.repository.UserRepository;

@Mapper(componentModel = "spring")
public abstract class TlPostDTOTOTlPost {
    private static UserRepository userRepository;

    public TlPost toTlPost(TlPostDTO tlPostDTO) {
        return toPost(tlPostDTO);
    }

    @Mappings({
            @Mapping(source = "createBy", target = "createBy", qualifiedByName = "mapCreateByReverse"),
    })
    protected abstract TlPost toPost(TlPostDTO tlPostDTO);

    @Named("mapCreateByReverse")
    User mapCreateByReverse(AuthorDTO authorDTO) {
        return authorDTO != null ? userRepository.findUserByLogin(authorDTO.getLogin()).orElseThrow() : null;
    }
}
