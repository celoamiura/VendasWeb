package com.projeto.vendasWeb.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.vendasWeb.models.Estado;

public interface EstadoRepositorio extends JpaRepository<Estado, Long> {
    
}
