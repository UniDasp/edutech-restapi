package com.edutech.cl.main.repository;


import com.edutech.cl.main.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para login
    Optional<Usuario> findByUsernameAndPassword(String username, String password);

    // También puedes agregar más métodos si los necesitas
    Optional<Usuario> findByUsername(String username);
}