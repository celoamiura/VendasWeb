package com.projeto.vendasWeb.repo;

import com.projeto.vendasWeb.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Usuario findByEmailAndPassword(String email, String senha);
    Usuario findByEmail(String email); 
}
