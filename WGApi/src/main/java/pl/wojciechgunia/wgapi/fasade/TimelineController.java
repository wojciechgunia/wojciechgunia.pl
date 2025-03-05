package pl.wojciechgunia.wgapi.fasade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wojciechgunia.wgapi.entity.TlPostDTO;
import pl.wojciechgunia.wgapi.mediator.TimelineMediator;

@RestController
@RequestMapping(value = "/api/v1/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineMediator timelineMediator;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResponseEntity<?> getTimelineList(@RequestParam(required = false) String name_like,
                                             @RequestParam(required = false) String _type,
                                             @RequestParam(required = false) String _technologies,
                                             @RequestParam(required = false) String it_fields,
                                             @RequestParam(required = false,defaultValue = "1") int _page,
                                             @RequestParam(required = false,defaultValue = "10") int _limit,
                                             @RequestParam(required = false,defaultValue = "asc") String _order) {
        return this.timelineMediator.getTimelineList(name_like, _type, _technologies, it_fields, _page, _limit, _order);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/element")
    public ResponseEntity<?> getTimelinePost(@RequestParam String uuid) {
        return this.timelineMediator.getTimelinePost(uuid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/element")
    public ResponseEntity<?> postTimelinePost(@RequestBody TlPostDTO tlPostDTO) {
        return this.timelineMediator.postTimelinePost(tlPostDTO);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/element")
    public ResponseEntity<?> putTimelinePost(@RequestBody TlPostDTO tlPostDTO) {
        return this.timelineMediator.putTimelinePost(tlPostDTO);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/element")
    public ResponseEntity<?> deleteTimelinePost(@RequestParam String uuid) {
        return this.timelineMediator.deleteTimelinePost(uuid);
    }

}
