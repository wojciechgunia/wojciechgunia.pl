package pl.wojciechgunia.wgapi.fasade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wojciechgunia.wgapi.entity.FileResponse;
import pl.wojciechgunia.wgapi.mediator.FileMediator;

@RestController
@RequestMapping(value = "/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final FileMediator mediatorFile;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveFile(@RequestBody MultipartFile multipartFile)
    {
        return mediatorFile.saveFile(multipartFile);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<FileResponse> deleteFile(@RequestParam String uuid)
    {
        return mediatorFile.deleteFile(uuid);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getFile(@RequestParam String uuid) {
        return mediatorFile.getFile(uuid);
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<FileResponse> activeFile(@RequestParam String uuid) {
        return mediatorFile.activeFile(uuid);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/get-all")
    public ResponseEntity<?> getAllFiles(@RequestParam(required = false,defaultValue = "1") int _page,
                                         @RequestParam(required = false,defaultValue = "10") int _limit) {
        return mediatorFile.getFiles(_page,_limit);
    }
}
