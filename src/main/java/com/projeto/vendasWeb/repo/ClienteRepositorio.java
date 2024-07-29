package com.projeto.vendasWeb.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.vendasWeb.models.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
	
}
