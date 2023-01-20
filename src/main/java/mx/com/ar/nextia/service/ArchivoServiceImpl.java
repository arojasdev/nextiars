package mx.com.ar.nextia.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoServiceImpl implements ArchivoService {

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo acceder a la carpeta uploads");
        }
    }

    @Override
    public void save(MultipartFile archivo) {
        try {
            
            Files.copy(archivo.getInputStream(), this.root.resolve(archivo.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Error!! No se pudo subir el archivo." + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            
            Path ruta = root.resolve(filename);
            Resource resource = new UrlResource(ruta.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new RuntimeException("Error!! No se pudo leer el archivo ");
            }

        }catch (MalformedURLException e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll(){
        try{
            return Files.walk(this.root,1).filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        }catch (RuntimeException | IOException e){
            throw new RuntimeException("Error!! No se pudieron cargar los archivos ");
        }
    }

    @Override
    public String deleteFile(String filename){
        try {
            
            Boolean delete = Files.deleteIfExists(this.root.resolve(filename));
                return "Eliminado";
        }catch (IOException e){
            e.printStackTrace();
            return "Error al Eliminar";
        }
    }
    
}
