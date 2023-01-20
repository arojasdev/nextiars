package mx.com.ar.nextia.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface ArchivoService {

    public void init();

    public void save(MultipartFile archivo);

    public Resource load(String archivo);

    public void deleteAll();

    public Stream<Path> loadAll();

    public String deleteFile(String filename) throws IOException;
}
