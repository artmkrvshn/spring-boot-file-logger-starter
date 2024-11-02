package org.spring.aop.example.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    private final Logger log = LoggerFactory.getLogger(StorageController.class);

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping()
    public ResponseEntity<List<FileEntity>> getAll() {
        return ResponseEntity.ok(storageService.findAll());
    }

    @GetMapping(value = "/{id}+")
    public ResponseEntity<FileEntity> getInfo(@PathVariable("id") Long id) {
        FileEntity fileEntity = storageService.findById(id);
        return ResponseEntity.ok(fileEntity);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable("id") Long id) {
        FileEntity fileEntity = storageService.findById(id);
        ByteArrayResource resource = new ByteArrayResource(fileEntity.getContent());
        return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, fileEntity.getContentDisposition() + "; filename=\"" + fileEntity.getFilename() + "\"")
                .header(CONTENT_TYPE, fileEntity.getContentType())
                .body(resource);
    }

    @PostMapping(value = "/save", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileEntity> save(@RequestParam("file") MultipartFile file,
                                           @RequestHeader(HttpHeaders.CONTENT_DISPOSITION) String contentDisposition) throws IOException {

        if (file == null || file.isEmpty()) throw new RuntimeException("File is empty");
        if (file.getResource().contentLength() > 1024 * 1024) throw new RuntimeException("File is too large");
        if (file.getOriginalFilename() == null) throw new RuntimeException("Filename is empty");

        FileEntity fileEntity = FileEntity.builder()
                .filename(file.getOriginalFilename())
                .extension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1))
                .contentType(file.getContentType())
                .contentDisposition(contentDisposition)
                .size(file.getResource().contentLength())
                .content(file.getResource().getContentAsByteArray())
                .build();

        FileEntity savedFile = storageService.save(fileEntity);
        return ResponseEntity.ok(savedFile);
    }

}
