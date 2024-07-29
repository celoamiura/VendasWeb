package com.projeto.vendasWeb.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.vendasWeb.models.Empresa;

public interface EmpresaRepositorio extends JpaRepository<Empresa, Long> {
    
}
