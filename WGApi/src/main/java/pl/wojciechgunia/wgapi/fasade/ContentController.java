package pl.wojciechgunia.wgapi.fasade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wojciechgunia.wgapi.entity.Code;
import pl.wojciechgunia.wgapi.entity.ContentDTO;
import pl.wojciechgunia.wgapi.entity.Language;
import pl.wojciechgunia.wgapi.entity.Response;
import pl.wojciechgunia.wgapi.mediator.ContentMediator;
import pl.wojciechgunia.wgapi.repository.LanguageRepository;

@RestController
@RequestMapping(value = "/api/v1/content")
@RequiredArgsConstructor
public class ContentController {
    public final ContentMediator contentMediator;
    public final LanguageRepository languageRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getContent(@RequestParam String code, @RequestParam(defaultValue = "en") String lang) {
        return this.contentMediator.getContent(code, lang);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Response> editContent(@RequestBody ContentDTO contentDTO) {
        return this.contentMediator.editContent(contentDTO);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/language")
    public ResponseEntity<?> getLanguages() {
        return ResponseEntity.ok(this.languageRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/language")
    public ResponseEntity<Response> addLanguages(@RequestBody Language language) {
        try {
            this.languageRepository.save(language);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/language")
    public ResponseEntity<Response> delLanguages(@RequestParam String code) {
        try {
            this.languageRepository.deleteByCode(code);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }
}