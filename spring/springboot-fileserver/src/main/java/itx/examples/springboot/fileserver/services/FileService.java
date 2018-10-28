package itx.examples.springboot.fileserver.services;

import itx.examples.springboot.fileserver.services.dto.FileList;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {

    Resource loadFileAsResource(String filePath) throws FileNotFoundException;

    FileList getFilesInfo(String filePath) throws IOException;

}
