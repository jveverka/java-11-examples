package itx.examples.springboot.fileserver.services;

import itx.examples.springboot.fileserver.services.dto.FileList;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    Resource loadFileAsResource(String filePath) throws FileNotFoundException;

    FileList getFilesInfo(String filePath) throws IOException;

    void saveFile(String filePath, InputStream inputStream) throws IOException;

    void delete(String filePath) throws IOException;

    void createDirectory(String filePath) throws IOException;

}
