package mx.com.ar.nextia.service;

import java.util.List;
import java.util.Set;
import mx.com.ar.nextia.domain.Usuario;
import mx.com.ar.nextia.domain.UsuarioRol;

public interface UsuariosService {
    
    public List<Usuario> listarUsuarios() throws Exception;
    
    public Usuario guardarUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws Exception;
    
    public Usuario obtenerUsuario(String userName) throws Exception;
    
    public void eliminarUsuario(Long idUsuario) throws Exception;
    
    public Usuario findByUsernamePassword (String username, String password) throws Exception;
    
    List<Usuario> findByEstatus(Boolean estatus);
    
    Usuario actualizarUsuario(Usuario usuario);
    
    Usuario findById(Long idUsuario);
    
}
