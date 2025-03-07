package pl.wojciechgunia.wgapi.mediator;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.wojciechgunia.wgapi.entity.FileDTO;
import pl.wojciechgunia.wgapi.entity.FileEntity;
import pl.wojciechgunia.wgapi.entity.FileResponse;
import pl.wojciechgunia.wgapi.entity.FilesDTO;
import pl.wojciechgunia.wgapi.exceptions.FtpConnectionException;
import pl.wojciechgunia.wgapi.service.FTPService;
import pl.wojciechgunia.wgapi.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class FileMediator {
    private final FTPService ftpService;
    private final FileService fileService;

    public ResponseEntity<?> saveFile(MultipartFile multipartFile) {
        try {
            String type = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1);
            if(type.equals("png") || type.equals("jpg") || type.equals("PNG") || type.equals("pdf")) {
                System.out.println(type);
                FileEntity fileEntity = ftpService.uploadFileToFTP(multipartFile);
                fileService.save(fileEntity);
                return ResponseEntity.ok(FileDTO.builder().uid(fileEntity.getUuid()).createAt(fileEntity.getCreateAt()).build());
            }
            return ResponseEntity.status(400).body(new FileResponse("Wrong file type"));
        } catch (IOException e) {
            return ResponseEntity.status(400).body(new FileResponse("File don't exist"));
        } catch (FtpConnectionException e1) {
            return ResponseEntity.status(400).body(new FileResponse("File couldn't be saved"));
        }
    }

    public ResponseEntity<FileResponse> deleteFile(String uid) {
        try {
            FileEntity fileEntity = fileService.findByUid(uid);
            if (fileEntity != null) {
                ftpService.deleteFile(fileEntity.getPath());
                return ResponseEntity.ok(new FileResponse("File deleted success"));
            } else {
                throw new IOException("File don't exist");
            }

        } catch (IOException e) {
            return ResponseEntity.status(400).body(new FileResponse("File don't exist"));
        } catch (FtpConnectionException e1) {
            return ResponseEntity.status(400).body(new FileResponse("File couldn't be deleted"));
        }

    }

    public ResponseEntity<?> getFile(String uid) {
        try {
            FileEntity fileEntity = fileService.findByUid(uid);
            if (fileEntity != null) {
                String type = fileEntity.getPath().substring(fileEntity.getPath().lastIndexOf(".")+1);
                HttpHeaders headers = new HttpHeaders();
                if ( type.equals("png") || type.equals("PNG"))
                    headers.setContentType(MediaType.IMAGE_PNG);
                if ( type.equals("jpg"))
                    headers.setContentType(MediaType.IMAGE_JPEG);
                if ( type.equals("pdf"))
                    headers.setContentType(MediaType.APPLICATION_PDF);
                return new ResponseEntity<>(ftpService.getFile(fileEntity).toByteArray(),headers, HttpStatus.OK);
            } else {
                throw new IOException("File don't exist");
            }

        } catch (IOException e) {
            return ResponseEntity.status(400).body(new FileResponse("File don't exist"));
        } catch (FtpConnectionException e1) {
            return ResponseEntity.status(400).body(new FileResponse("File couldn't be downloaded"));
        }
    }

    public ResponseEntity<FileResponse> activeFile(String uid) {
        FileEntity fileEntity = fileService.findByUid(uid);
        if (fileEntity != null) {
            fileEntity.setUsed(true);
            fileService.save(fileEntity);
            return ResponseEntity.ok(new FileResponse("File successfully activated"));
        } else {
            return ResponseEntity.status(400).body(new FileResponse("File don't exist"));
        }
    }

    public ResponseEntity<?> getFiles(int page, int limit) {
        List<FileEntity> products = fileService.getFiles(page, limit);
        List<FileDTO> fileDTOS = new ArrayList<>();
        products.forEach(value -> fileDTOS.add(new FileDTO(value.getUuid(), value.getCreateAt(), value.getLang(), value.getType())));
        long totalCount = fileService.countFiles();
        FilesDTO filesDTO = new FilesDTO(fileDTOS);
        return ResponseEntity.ok().header("X-Total-Count",String.valueOf(totalCount)).body(filesDTO);
    }
}
