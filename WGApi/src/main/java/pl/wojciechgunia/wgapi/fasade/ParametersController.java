package pl.wojciechgunia.wgapi.fasade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wojciechgunia.wgapi.entity.*;
import pl.wojciechgunia.wgapi.service.ParametersService;

@RestController
@RequestMapping(value = "/api/v1/parameters")
@RequiredArgsConstructor
public class ParametersController {
    private final ParametersService parametersService;

    @RequestMapping(method = RequestMethod.GET, value = "/type-list")
    public ResponseEntity<?> getTypes() {
        return this.parametersService.getTypes();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/technologyList")
    public ResponseEntity<?> getTechnologies() {
        return this.parametersService.getTechnologies();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/it-field-list")
    public ResponseEntity<?> getItField() {
        return this.parametersService.getItField();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/technologyList")
    public ResponseEntity<?> postTechnologies(@RequestBody TechnologyDTO technology) {
        return this.parametersService.postTechnology(technology);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/it-field-list")
    public ResponseEntity<?> postItField(@RequestBody ItFieldDTO itField) {
        return this.parametersService.postItField(itField);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/technologyList")
    public ResponseEntity<?> putTechnologies(@RequestBody TechnologyDTO technology) {
        return this.parametersService.putTechnology(technology);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/it-field-list")
    public ResponseEntity<?> putItField(@RequestBody ItFieldDTO itField) {
        return this.parametersService.putItField(itField);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/technologyList")
    public ResponseEntity<?> deleteTechnologies(@RequestParam String name) {
        return this.parametersService.deleteTechnology(name);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/it-field-list")
    public ResponseEntity<?> deleteItField(@RequestParam String name) {
        return this.parametersService.deleteItField(name);
    }
}
