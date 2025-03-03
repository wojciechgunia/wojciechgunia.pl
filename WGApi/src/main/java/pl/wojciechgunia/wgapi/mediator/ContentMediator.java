package pl.wojciechgunia.wgapi.mediator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.wojciechgunia.wgapi.entity.*;
import pl.wojciechgunia.wgapi.service.ContentService;
import pl.wojciechgunia.wgapi.translator.ContentDTOToContent;
import pl.wojciechgunia.wgapi.translator.ContentToContentFileDTO;
import pl.wojciechgunia.wgapi.translator.ContentToContentTextDTO;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContentMediator {

    @Value("${backend.url}")
    private String BACKEND_URL;

    private final ContentService contentService;
    private final ContentToContentFileDTO contentToContentFileDTO;
    private final ContentToContentTextDTO contentToContentTextDTO;
    private final ContentDTOToContent contentDTOToContent;

    private ResponseEntity<?> checkContent(Content content) {
        if (content.getType().equals("File")) {
            ContentFileDTO contentFileDTO = this.contentToContentFileDTO.toContentFileDTO(content);
            contentFileDTO.setFile_url(BACKEND_URL + "/file/" + contentFileDTO.getFile_url());
            return ResponseEntity.ok().body(contentFileDTO);
        } else {
            ContentTextDTO contentTextDTO = this.contentToContentTextDTO.toContentTextDTO(content);
            return ResponseEntity.ok().body(contentTextDTO);
        }
    }

    public ResponseEntity<?> getContent(String code, String lang) {
        Optional<Content> content = this.contentService.getContent(code, lang);
        if (content.isPresent()) {
            return checkContent(content.get());
        } else {
            content = this.contentService.getContent(code, "en");
            if (content.isPresent()) {
                return checkContent(content.get());
            } else {
                return ResponseEntity.status(400).body(new Response(Code.E2));
            }
        }
    }

    public ResponseEntity<Response> editContent(ContentDTO contentDTO) {
        try {
            if((Objects.equals(contentDTO.getType(), "Text") && contentDTO.getText().isEmpty()) || (Objects.equals(contentDTO.getType(), "File") && contentDTO.getFile_url().isEmpty()) || !(Objects.equals(contentDTO.getType(), "File") || Objects.equals(contentDTO.getType(), "Text")))
            {
                return ResponseEntity.status(400).body(new Response(Code.E3));
            }
            this.contentService.editContent(this.contentDTOToContent.toContent(contentDTO));
            return ResponseEntity.status(200).body(new Response(Code.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }
}
