package mx.com.ar.nextia.repository;

import mx.com.ar.nextia.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
    
}