package pl.wojciechgunia.wgapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.wojciechgunia.wgapi.entity.*;
import pl.wojciechgunia.wgapi.repository.ItFieldRepository;
import pl.wojciechgunia.wgapi.repository.TechnologyRepository;
import pl.wojciechgunia.wgapi.repository.TypesRepository;

@Service
@RequiredArgsConstructor
public class ParametersService {

    private final TypesRepository typesRepository;
    private final TechnologyRepository technologyRepository;
    private final ItFieldRepository itFieldRepository;

    public ResponseEntity<?> getTypes() {
        try {
            return ResponseEntity.ok(this.typesRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }


    public ResponseEntity<?> getTechnologies() {
        try {
            return ResponseEntity.ok(this.technologyRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<?> getItField() {
        try {
            return ResponseEntity.ok(this.itFieldRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }


    public ResponseEntity<Response> postTechnology(TechnologyDTO technology) {
        try {
            Technology technology_new = new Technology();
            technology_new.setName(technology.getName());
            technology_new.setImage_url(technology.getImage_url());
            this.technologyRepository.save(technology_new);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception _) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<Response> postItField(ItFieldDTO itField) {
        try {
            ItField it_field_new = new ItField();
            it_field_new.setImage_url(itField.getImage_url());
            this.itFieldRepository.save(it_field_new);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception _) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<Response> putTechnology(TechnologyDTO technology) {
        try {
            Technology technology_edit = this.technologyRepository.findTechnologiesByName(technology.getName()).orElseThrow();
            technology_edit.setImage_url(technology.getImage_url());
            this.technologyRepository.save(technology_edit);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception _) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<Response> putItField(ItFieldDTO itField) {
        try {
            ItField it_field_edit = this.itFieldRepository.findItFieldByName(itField.getName()).orElseThrow();
            it_field_edit.setImage_url(itField.getImage_url());
            this.itFieldRepository.save(it_field_edit);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception _) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<Response> deleteTechnology(String name) {
        try {
            this.technologyRepository.deleteByName(name);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception _) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<Response> deleteItField(String name) {
        try {
            this.itFieldRepository.deleteByName(name);
            return ResponseEntity.ok(new Response(Code.SUCCESS));
        } catch (Exception _) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }
}
