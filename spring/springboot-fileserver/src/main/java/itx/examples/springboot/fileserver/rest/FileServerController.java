package itx.examples.springboot.fileserver.rest;

import itx.examples.springboot.fileserver.services.FileService;
import itx.examples.springboot.fileserver.services.dto.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(path = FileServerController.URI_PREFIX)
public class FileServerController {

    private static final Logger LOG = LoggerFactory.getLogger(FileServerController.class);

    public static final String URI_PREFIX = "/services/files";
    public static final String LIST_PREFIX = "/list/";
    public static final String DOWNLOAD_PREFIX = "/download/";

    @Autowired
    private FileService fileService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping(DOWNLOAD_PREFIX + "**")
    public ResponseEntity<Resource> downloadFile() {
        try {
            String contextPath = httpServletRequest.getRequestURI();
            String filePath = contextPath.substring((URI_PREFIX + DOWNLOAD_PREFIX).length());
            LOG.info("downloadFile: {}", filePath);
            Resource resource = fileService.loadFileAsResource(filePath);
            String contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(FileServerController.LIST_PREFIX + "**")
    public ResponseEntity<FileList> getFiles() {
        try {
            String contextPath = httpServletRequest.getRequestURI();
            String filePath = contextPath.substring((URI_PREFIX + LIST_PREFIX).length());
            LOG.info("getFiles: {}", filePath);
            FileList fileInfo = fileService.getFilesInfo(filePath);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fileInfo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
