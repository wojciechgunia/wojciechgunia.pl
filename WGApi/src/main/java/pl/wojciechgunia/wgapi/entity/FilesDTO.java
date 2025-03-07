package pl.wojciechgunia.wgapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FilesDTO {
    private List<FileDTO> fileDTOS;
}
