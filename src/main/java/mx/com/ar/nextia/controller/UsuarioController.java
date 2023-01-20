package mx.com.ar.nextia.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mx.com.ar.nextia.domain.Archivo;
import mx.com.ar.nextia.domain.Rol;
import mx.com.ar.nextia.domain.Usuario;
import mx.com.ar.nextia.domain.UsuarioRol;
import mx.com.ar.nextia.service.ArchivoService;
import mx.com.ar.nextia.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {
    
    @Autowired
    private UsuariosService usuarioService;
    
    @PostMapping("/")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) throws Exception {
            Set<UsuarioRol> usuarioRoles = new HashSet<>();
            
            Rol rol = new Rol();
            rol.setIdRol(1L);
            rol.setNombre("ADMIN");
            
            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setRol(rol);
            usuarioRol.setUsuario(usuario);
            
            usuarioRoles.add(usuarioRol);
            
            return usuarioService.guardarUsuario(usuario, usuarioRoles);
    }
    
    @GetMapping("/{username}")
    public Usuario obtenerUsuario(@PathVariable("username") String username) throws Exception {
        return usuarioService.obtenerUsuario(username);
    }
    
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable("id") Long id) throws Exception {
        usuarioService.eliminarUsuario(id);
    }
    
    @GetMapping("/activo")
    public List<Usuario> listarUsuariosActivos(){
        return usuarioService.findByEstatus(true);
    }
    
    @PutMapping("/")
    public ResponseEntity<Usuario> actualizarUsuario(@RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.actualizarUsuario(usuario));
    }
    
    @GetMapping("/buscar/{idUsuario}")
    public Usuario obtenerUsuarioById(@PathVariable("idUsuario") Long idUsuario){
        return usuarioService.findById(idUsuario);
    }
    
    //endpoints para manejo de archivos
    @Autowired
    ArchivoService archivoService;
    
    @PostMapping("/archivos/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("files")MultipartFile[] files) {
        
        String mensaje = "";
        
        try {
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {
                archivoService.save(file);
                fileNames.add(file.getOriginalFilename());
            });

            mensaje = "Archivos cargados con exito " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);
        } catch (Exception e){
            mensaje = "Error al cargar los archivos";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(mensaje);
        }
    }
    
    @GetMapping("/archivos/files")
    public ResponseEntity<List<Archivo>> getFiles() {
        
        List<Archivo> fileInfos = archivoService.loadAll().map(path -> {
          String filename = path.getFileName().toString();
          String url = MvcUriComponentsBuilder.fromMethodName(UsuarioController.class, "getFile",
                  path.getFileName().toString()).build().toString();
          return new Archivo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
    
    @GetMapping("/archivos/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) { 
        
        Resource file = archivoService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename() + "\"").body(file);
    }
    
    @GetMapping("/archivos/delete/{filename:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        String mensaje = "";
        try {
            mensaje = archivoService.deleteFile(filename);
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);
        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(mensaje);
        }
    }
    
}
