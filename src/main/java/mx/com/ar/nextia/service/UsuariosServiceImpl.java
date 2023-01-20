package mx.com.ar.nextia.service;

import java.util.List;
import java.util.Set;
import mx.com.ar.nextia.repository.UsuarioRepository;
import mx.com.ar.nextia.domain.Usuario;
import mx.com.ar.nextia.domain.UsuarioRol;
import mx.com.ar.nextia.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuariosServiceImpl implements UsuariosService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() throws Exception {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario, Set<UsuarioRol> usuarioRoles) throws Exception {
        
        Usuario usuarioAux = usuarioRepository.findByUsername(usuario.getUsername());
        if(usuarioAux != null) {
            throw new Exception("El usuario ya existe");
        } else {
            for(UsuarioRol usuarioRol : usuarioRoles) {
                rolRepository.save(usuarioRol.getRol());
            }
            usuario.getUsuarioRoles().addAll(usuarioRoles);
            usuarioAux = usuarioRepository.save(usuario);
        }
        return usuarioAux;
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long idUsuario) throws Exception {
        
        usuarioRepository.deleteById(idUsuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuario(String userName) throws Exception {
        return usuarioRepository.findByUsername(userName);
    }
    
    @Transactional(readOnly = true)
    public Usuario findByUsernamePassword(String username, String password) throws Exception {
        return usuarioRepository.findByUsernamePassword(username, password);
    }

    @Override
    public List<Usuario> findByEstatus(Boolean estatus) {
        return usuarioRepository.findByEstatus(estatus);
    }
    
    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario findById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).get();
    }
    
}