package mx.com.ar.nextia.repository;

import java.util.List;
import mx.com.ar.nextia.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    public Usuario findByUsername(String username);
    
    @Query(value = "select u.* from usuario u where u.username = :username and u.password = :password", nativeQuery = true)
    Usuario findByUsernamePassword(@Param("username") String username, @Param("password") String password);
    
    List<Usuario> findByEstatus(Boolean estatus);
    
}