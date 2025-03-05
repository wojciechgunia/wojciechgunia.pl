package pl.wojciechgunia.wgapi.mediator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.wojciechgunia.wgapi.entity.Code;
import pl.wojciechgunia.wgapi.entity.Response;
import pl.wojciechgunia.wgapi.entity.TlPost;
import pl.wojciechgunia.wgapi.entity.TlPostDTO;
import pl.wojciechgunia.wgapi.service.TimelineService;
import pl.wojciechgunia.wgapi.translator.TlPostDTOTOTlPost;
import pl.wojciechgunia.wgapi.translator.TlPostToTlPostDTO;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimelineMediator {
    private final TimelineService timelineService;
    private final TlPostDTOTOTlPost tlPostDTOTOTlPost;
    private final TlPostToTlPostDTO tlPostToTlPostDTO;

    @Value("${backend.url}")
    private String BACKEND_URL;

    public ResponseEntity<?> getTimelineList(String nameLike, String type, String technologies, String itFields, int page, int limit, String order) {
        try {
            List<TlPost> tlPosts = this.timelineService.getTimelineList(nameLike, type, technologies, itFields, page, limit, order);
            List<TlPostDTO> postDTOList = new ArrayList<>();
            for (TlPost tlPost : tlPosts) {
                TlPostDTO tlPostDTO = this.tlPostToTlPostDTO.toTlPostDTO(tlPost);
                List<String> images = new ArrayList<>();
                images.add(BACKEND_URL + "/file/" + tlPostDTO.getImageUrls().getFirst());
                tlPostDTO.setImageUrls(images);
                tlPostDTO.setFileUrls(new ArrayList<>());
                postDTOList.add(tlPostDTO);
            }
            return ResponseEntity.ok(postDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<?> getTimelinePost(String uuid) {
        try {
            TlPost tlPost = this.timelineService.getTimelinePost(uuid);
            List<String> images = new ArrayList<>();
            for(int i = 0; i<tlPost.getImageUrls().size(); i++)
                images.add(BACKEND_URL + "/file/" + tlPost.getImageUrls().get(i));
            tlPost.setImageUrls(images);
            List<String> files = new ArrayList<>();
            for(int i = 0; i<tlPost.getFileUrls().size(); i++)
                files.add(BACKEND_URL + "/file/" + tlPost.getFileUrls().get(i));
            tlPost.setFileUrls(files);
            return ResponseEntity.ok(this.tlPostToTlPostDTO.toTlPostDTO(tlPost));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new Response(Code.E2));
        }
    }

    public ResponseEntity<?> postTimelinePost(TlPostDTO tlPostDTO) {
        return this.timelineService.postTimelinePost(this.tlPostDTOTOTlPost.toTlPost(tlPostDTO));
    }

    public ResponseEntity<?> putTimelinePost(TlPostDTO tlPostDTO) {
        return this.timelineService.putTimelinePost(this.tlPostDTOTOTlPost.toTlPost(tlPostDTO));
    }

    public ResponseEntity<?> deleteTimelinePost(String uuid) {
        return this.timelineService.deleteTimelinePost(uuid);
    }
}
