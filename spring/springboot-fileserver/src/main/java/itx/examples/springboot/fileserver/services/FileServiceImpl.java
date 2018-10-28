package itx.examples.springboot.fileserver.services;

import itx.examples.springboot.fileserver.config.FileServerConfig;
import itx.examples.springboot.fileserver.services.dto.DirectoryInfo;
import itx.examples.springboot.fileserver.services.dto.FileInfo;
import itx.examples.springboot.fileserver.services.dto.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileServiceImpl(FileServerConfig fileServerConfig) {
        fileStorageLocation = Paths.get(fileServerConfig.getHome())
                .toAbsolutePath().normalize();
    }

    @Override
    public Resource loadFileAsResource(String filePath) throws FileNotFoundException {
        LOG.info("loadFileAsResource: {}", filePath);
        try {
            Path resolvedFilePath = this.fileStorageLocation.resolve(filePath).normalize();
            Resource resource = new UrlResource(resolvedFilePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + filePath);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + filePath);
        }
    }

    @Override
    public FileList getFilesInfo(String filePath) throws IOException {
        LOG.info("getFilesInfo: {}", filePath);
        FileList fileList = new FileList(filePath);
        Path resolvedFilePath = this.fileStorageLocation.resolve(filePath).normalize();
        try (Stream<Path> filesWalk = Files.walk(resolvedFilePath, 1)) {
            filesWalk.forEach(fw -> {
                if (resolvedFilePath.endsWith(fw)) {
                    //skip parent directory
                } else if (Files.isDirectory(fw)) {
                    fileList.add(new DirectoryInfo(fw.getFileName().toString()));
                } else {
                    fileList.add(new FileInfo(fw.getFileName().toString()));
                }
            });
        }
        return fileList;
    }

}
