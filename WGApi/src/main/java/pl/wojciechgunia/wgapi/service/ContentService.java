package pl.wojciechgunia.wgapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wojciechgunia.wgapi.entity.Content;
import pl.wojciechgunia.wgapi.repository.ContentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    public Optional<Content> getContent(String code, String lang) {
        return this.contentRepository.findContentByCodeAndLang(code, lang);
    }

    public void editContent(Content content) {
        this.contentRepository.save(content);
    }
}
